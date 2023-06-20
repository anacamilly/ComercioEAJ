package ufrn.com.comercioeaj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.services.UsuariosService;

@Controller
public class UsuariosController {


    UsuariosService service;


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
    public String doSalvarUsuario(@ModelAttribute Usuarios u, RedirectAttributes redirectAttributes){
        service.create(u);

        redirectAttributes.addFlashAttribute("mensagem", "Cadastro concluído com sucesso.");
        return "redirect:/login";
    }
}
