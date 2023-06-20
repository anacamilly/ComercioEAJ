package ufrn.com.comercioeaj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.UsuariosRepository;

import java.util.List;

@Controller
public class VendedoresController {

    private final UsuariosRepository usuariosRepository;

    public VendedoresController(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @GetMapping("/vendedores")
    public String getVendedores(Model model) {
        List<Usuarios> vendedores = usuariosRepository.findByIsVendedorTrue();
        model.addAttribute("vendedores", vendedores);
        return "vendedores/lista.html";
    }
}
