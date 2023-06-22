package ufrn.com.comercioeaj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.services.FileStorageService;
import ufrn.com.comercioeaj.services.UsuariosService;

@Controller
public class UsuariosController {

    UsuariosService service;

    @Autowired
    private FileStorageService fileStorageService;

    public UsuariosController(UsuariosService service) {
        this.service = service;
    }

    @GetMapping("/cadastre-se")
    public String doCadastrarUsuario(Model model){

        Usuarios u = new Usuarios();
        model.addAttribute("usuario", u);

        return "usuarios/cadastre-se";
    }

    @PostMapping("/salvar")
    public String doSalvarUsuario(@ModelAttribute Usuarios u, @RequestParam(name = "file") MultipartFile file, RedirectAttributes redirectAttributes){
        u.setImagemUri(file.getOriginalFilename());
        service.editar(u);
        fileStorageService.save(file);

        redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
        service.create(u);

        return "redirect:/login";
    }
}