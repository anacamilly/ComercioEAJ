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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProdutosController {

    UsuariosService usuariosService;
    ProdutosService produtosService;
    private final FileStorageService fileStorageService;

    public ProdutosController(ProdutosService produtosService, UsuariosService usuariosService,FileStorageService fileStorageService) {
        this.produtosService = produtosService;
        this.fileStorageService = fileStorageService;
        this.usuariosService = usuariosService;
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
            p.setVendedor(vendedor);

            p.setImagemUri(file.getOriginalFilename());
            produtosService.editar(p);
            fileStorageService.save(file);

            redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
            produtosService.salvarProduto(p);
            return "redirect:/meus-produtos";
        }
    }

    @RequestMapping(value = {"/catalogo-produtos"}, method = RequestMethod.GET)
    public String getCatalogo(Model model, Principal principal) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Produtos> produtosList = produtosService.listarProdutos();

        model.addAttribute("listarProdutos", produtosList);

        return "produtos/catalogo.html";
    }

    @GetMapping("/detalhes-produto/{id}")
    public String getDetalhesProduto(@PathVariable Long id, Model model) {
        Optional<Produtos> produtoOptional = produtosService.findById(id);

        if (produtoOptional.isEmpty()) {
            return "error"; // Página de erro caso o produto não seja encontrado
        }

        Produtos produto = produtoOptional.get();
        Usuarios vendedor = produto.getVendedor(); // Obter o objeto vendedor do produto

        model.addAttribute("produto", produto);
        model.addAttribute("vendedor", vendedor); // Adicionar o objeto vendedor ao modelo

        return "produtos/detalhes-produto"; // Nome do arquivo HTML
    }


    @RequestMapping(value = {"/meus-produtos", "/produtos"}, method = RequestMethod.GET)
    public String getProdutos(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Usuarios) {
            Usuarios usuarioLogado = (Usuarios) authentication.getPrincipal();
            Long usuarioId = usuarioLogado.getId();

            // Utilize the logged-in user's ID to retrieve all products associated with them
            List<Produtos> produtosDoUsuarioLogado = produtosService.buscarProdutosPorUsuarioId(usuarioId);

            // Filter out the products that have a non-null value for the "deleted" field
            List<Produtos> produtosAtivos = produtosDoUsuarioLogado.stream()
                    .filter(produto -> produto.getDeleted() == null)
                    .collect(Collectors.toList());

            // Add the list of active products to the model
            model.addAttribute("produtos", produtosAtivos);
        } else {
            // The authentication is not valid or the principal is not of type Usuarios
            // Handle this situation accordingly
        }

        return "produtos/gerenciar-produtos.html";
    }

    @GetMapping(value = "/excluir/{id}")
    public String doExcluirProduto(@PathVariable long id, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("mensagem", "Produto excluido com sucesso.");

        produtosService.excluirProduto(id);
        return "redirect:/meus-produtos";
    }

}
