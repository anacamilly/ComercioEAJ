package ufrn.com.comercioeaj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.UsuariosRepository;
import ufrn.com.comercioeaj.services.ProdutosService;
import ufrn.com.comercioeaj.services.UsuariosService;

import java.util.List;
import java.util.Optional;

@Controller
public class VendedoresController {

    private final UsuariosRepository usuariosRepository;
    private final UsuariosService service;


    public VendedoresController(UsuariosRepository usuariosRepository, UsuariosService service) {
        this.usuariosRepository = usuariosRepository;
        this.service = service;
    }

    @GetMapping("/vendedores")
    public String getVendedores(Model model) {
        List<Usuarios> vendedores = usuariosRepository.findByIsVendedorTrue();
        model.addAttribute("vendedores", vendedores);
        return "vendedores/lista.html";
    }

    @GetMapping("/perfil-vendedor/{id}")
    public String getPerfilVendedor(@PathVariable Long id, Model model) {
        Optional<Usuarios> vendedorOptional = service.findById(id);

        if (vendedorOptional.isEmpty()) {
            return "error"; // Página de erro caso o vendedor não seja encontrado
        }

        Usuarios vendedor = vendedorOptional.get();

        model.addAttribute("vendedor", vendedor);

        return "vendedores/perfil-vendedor";

    }

}
