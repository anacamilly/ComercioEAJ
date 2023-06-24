package ufrn.com.comercioeaj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.services.FileStorageService;
import ufrn.com.comercioeaj.services.UsuariosService;

import java.util.Optional;

@Controller
public class UsuariosController {

    UsuariosService service;

    @Autowired
    private FileStorageService fileStorageService;

    public UsuariosController(UsuariosService service) {
        this.service = service;
    }

    @GetMapping("/meu-perfil")
    public String doPerfil(Model model) {
        // Obter o objeto Authentication do Spring Security
        // Obter o ID do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuarioLogado = ((Usuarios) authentication.getPrincipal()).getId();

        // Exemplo fictício de obtenção das informações do usuário com base no id
        Optional<Usuarios> usuario = service.findById(idUsuarioLogado);

        model.addAttribute("usuario", usuario.orElse(null));


        return "usuarios/meu-perfil";
    }

    @GetMapping("/meu-perfil/atualizar/{id}")
    public String doAtualizarPerfil(@PathVariable(name = "id") Long id, Model model) {
        Optional<Usuarios> usuarios = service.findById(id);

        if (usuarios.isPresent()){
            model.addAttribute("usuario", usuarios.get());
        }else{
            return "redirect:/meu-perfil";
        }

        return "usuarios/editar-perfil";
    }

    @PostMapping("/meu-perfil/atualizar/salvar")
    public String doSalvarPerfilUsuario(@ModelAttribute Usuarios u, @RequestParam(name = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes){
        if (file != null && !file.isEmpty()) {
            u.setImagemUri(file.getOriginalFilename());
            fileStorageService.save(file);
        } else {
            // Manter o valor existente do campo imagemUri
            Optional<Usuarios> existingUser = service.findById(u.getId());
            if (existingUser.isPresent()) {
                u.setImagemUri(existingUser.get().getImagemUri());
            }
        }

        service.editar(u);
        redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
        return "redirect:/meu-perfil";
    }

    @GetMapping("/cadastre-se")
    public String doCadastrarUsuario(Model model){

        Usuarios u = new Usuarios();
        model.addAttribute("usuario", u);

        return "usuarios/cadastre-se";
    }

    @PostMapping("/cadastre-se/salvar")
    public String doSalvarUsuario(@ModelAttribute Usuarios u, @RequestParam(name = "file") MultipartFile file, RedirectAttributes redirectAttributes){
        u.setImagemUri(file.getOriginalFilename());
        service.editar(u);
        fileStorageService.save(file);

        redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
        service.create(u);

        return "redirect:/login";
    }
}