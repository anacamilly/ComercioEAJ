package ufrn.com.comercioeaj.controllers;

import jakarta.validation.Valid;
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

import java.security.Principal;
import java.util.List;

@Controller
public class ProdutosController {

    UsuariosService usuariosService;
    ProdutosService produtosService;
    private final FileStorageService fileStorageService;

    public ProdutosController(ProdutosService produtosService, FileStorageService fileStorageService) {
        this.produtosService = produtosService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/cadastro-produto")
    public String getCadastrarProduto(Model model) {
        Produtos p = new Produtos();
        model.addAttribute("produto", p);
        return "produtos/cadastro.html";
    }

    @PostMapping("/salvar-produto")
    public String doSalvar(@ModelAttribute @Valid Produtos p, Errors errors, @RequestParam(name = "file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "produtos/cadastro.html";
        } else {

            // Obter o ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long idUsuarioLogado = ((Usuarios) authentication.getPrincipal()).getId();

            // Definir o ID do usuário no objeto Produtos
            Usuarios vendedor = new Usuarios();
            vendedor.setId(idUsuarioLogado);
            p.setId_vendedor(vendedor);

            p.setImagemUri(file.getOriginalFilename());
            produtosService.editar(p);
            fileStorageService.save(file);

            redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
            produtosService.salvarProduto(p);
            return "redirect:/catalogo-produtos";
        }
    }

    @RequestMapping(value = {"/catalogo-produtos"}, method = RequestMethod.GET)
    public String getCatalogo(Model model, Principal principal) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Produtos> produtosList = produtosService.listarProdutos();

        model.addAttribute("listarProdutos", produtosList);

        return "produtos/catalogo.html";
    }

    @RequestMapping(value = {"/gerenciar-produtos", "/produtos"}, method = RequestMethod.GET)
    public String getProdutos(Model model, Principal principal) {

        return "produtos/gerenciar-produtos.html";
    }

}
