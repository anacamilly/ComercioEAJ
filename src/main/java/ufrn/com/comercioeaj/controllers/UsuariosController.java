package ufrn.com.comercioeaj.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.services.FileStorageService;
import ufrn.com.comercioeaj.services.ProdutosService;
import ufrn.com.comercioeaj.services.UsuariosService;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UsuariosController {

    UsuariosService service;

    ProdutosService produtosService;
    @Autowired
    private FileStorageService fileStorageService;

    public UsuariosController(UsuariosService service, ProdutosService produtosService) {
        this.service = service;
        this.produtosService = produtosService;
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
    public String doSalvarPerfilUsuario(@ModelAttribute Usuarios u, @RequestParam(name = "file", required = false) MultipartFile file,  @RequestParam(name = "croppedImage", required = false) String croppedImage, RedirectAttributes redirectAttributes) throws IOException {
        String whatsapp = u.getWhatsapp().replaceAll("[\\s()+-]", "");

        // Define o número de telefone modificado no objeto de usuário
        u.setWhatsapp(whatsapp);

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

        if (croppedImage != null && !croppedImage.isEmpty()) {
            // A imagem foi cortada, salve-a diretamente
            byte[] imageData = decodeBase64Image(croppedImage);
            String fileExtension = getFileExtension(file);
            String imageName = generateUniqueFileName(fileExtension);
            String imagePath = fileStorageService.saveCroppedImage(imageData, imageName);
            u.setImagemUri(imagePath);
        } else if (file != null && !file.isEmpty()) {
            // A imagem não foi cortada, salve-a normalmente
            String fileExtension = getFileExtension(file);
            String imageName = generateUniqueFileName(fileExtension);
            String imagePath = fileStorageService.save2(file, imageName);
            u.setImagemUri(imagePath);
        }

        Usuarios updatedUser = service.editar(u);

        // Atualizar as informações do usuário no objeto Principal do Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principalUser = (Usuarios) authentication.getPrincipal();
        principalUser.setNomeSocial(updatedUser.getNomeSocial());
        principalUser.setImagemUri(updatedUser.getImagemUri());
        // Atualize outras informações necessárias

        redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
        return "redirect:/meu-perfil";
    }

    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return FilenameUtils.getExtension(originalFileName);
    }

    private String generateUniqueFileName(String fileExtension) {
        String uniqueFileName = UUID.randomUUID().toString();
        return uniqueFileName + "." + fileExtension;
    }


    private byte[] decodeBase64Image(String croppedImage) {
        String[] parts = croppedImage.split(",");
        String imageDataString = parts[1];
        return Base64.getDecoder().decode(imageDataString);
    }

    @GetMapping("/usuario/alterar-foto/{id}")
    public String getEditarFoto(@PathVariable(name = "id") Long id, Model model){

        Optional<Usuarios> usuario = service.findById(id);

        if (usuario.isPresent()){
            model.addAttribute("usuario", usuario.get());
        }else{
            return "redirect:/meu-perfil";
        }

        return "/usuarios/alterar-imagem.html";
    }

    @GetMapping("/cadastre-se")
    public String doCadastrarUsuario(Model model){
        Usuarios u = new Usuarios();
        model.addAttribute("usuario", u);

        return "usuarios/cadastre-se";
    }

    @PostMapping("/cadastre-se/salvar")
    public String doSalvarUsuario(@ModelAttribute Usuarios u, @RequestParam(name = "file") MultipartFile file, @RequestParam(name = "croppedImage", required = false) String croppedImage, RedirectAttributes redirectAttributes) throws IOException {
        String whatsapp = u.getWhatsapp().replaceAll("[\\s()+-]", "");

        // Define o número de telefone modificado no objeto de usuário
        u.setWhatsapp(whatsapp);

        if (croppedImage != null && !croppedImage.isEmpty()) {
            // A imagem foi cortada, salve-a diretamente
            byte[] imageData = decodeBase64Image(croppedImage);
            String fileExtension = getFileExtension(file);
            String imageName = generateUniqueFileName(fileExtension);
            String imagePath = fileStorageService.saveCroppedImage(imageData, imageName);
            u.setImagemUri(imagePath);
        } else if (file != null && !file.isEmpty()) {
            // A imagem não foi cortada, salve-a normalmente
            String fileExtension = getFileExtension(file);
            String imageName = generateUniqueFileName(fileExtension);
            String imagePath = fileStorageService.save2(file, imageName);
            u.setImagemUri(imagePath);
        }

        service.editar(u);
        fileStorageService.save(file);

        redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso.");
        service.create(u);

        return "redirect:/login";
    }

    @GetMapping(value = "/usuario/excluir/{id}")
    public String doExcluirConta(@PathVariable long id, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("mensagem", "Conta excluida com sucesso.");

        produtosService.excluirTodosProdutos(id);
        service.excluirConta(id);
        return "redirect:/logout";
    }


    @GetMapping("/meu-perfil/configuracoes")
    public String getConfiguracoes(Model model) {


        // Obter o objeto Authentication do Spring Security
        // Obter o ID do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuarioLogado = ((Usuarios) authentication.getPrincipal()).getId();

        // Exemplo fictício de obtenção das informações do usuário com base no id
        Optional<Usuarios> usuario = service.findById(idUsuarioLogado);

        model.addAttribute("usuario", usuario.orElse(null));

        return "/usuarios/conta-configuracoes.html";
    }

}
