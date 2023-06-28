package ufrn.com.comercioeaj.controllers;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

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

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

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

    @GetMapping("/produtos/cadastro")
    public String getCadastrarProduto(Model model) {
        Produtos p = new Produtos();
        model.addAttribute("produto", p);
        return "produtos/cadastro.html";
    }

    @PostMapping("/produtos/cadastro/salvar")
    public String doSalvarCadastro(@ModelAttribute @Valid Produtos p, Errors errors, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam(name = "croppedImage", required = false) String croppedImage, RedirectAttributes redirectAttributes) throws IOException {
        if (errors.hasErrors()) {
            return "produtos/cadastro.html";
        } else {
            // Obter o ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long idUsuarioLogado = ((Usuarios) authentication.getPrincipal()).getId();

            // Definir o ID do usuário no objeto Produto
            Usuarios vendedor = new Usuarios();
            vendedor.setId(idUsuarioLogado);
            p.setVendedor(vendedor);

            if (croppedImage != null && !croppedImage.isEmpty()) {
                // A imagem foi cortada, salve-a diretamente
                byte[] imageData = decodeBase64Image(croppedImage);
                String fileExtension = getFileExtension(file);
                String imageName = generateUniqueFileName(fileExtension);
                String imagePath = fileStorageService.saveCroppedImage(imageData, imageName);
                p.setImagemUri(imagePath);
            } else if (file != null && !file.isEmpty()) {
                // A imagem não foi cortada, salve-a normalmente
                String fileExtension = getFileExtension(file);
                String imageName = generateUniqueFileName(fileExtension);
                String imagePath = fileStorageService.save2(file, imageName);
                p.setImagemUri(imagePath);
            }

            p.setData_cadastro(Date.valueOf(LocalDate.now()));
            produtosService.editar(p);
            redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
            produtosService.salvarProduto(p);
            return "redirect:/meus-produtos";
        }
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


    @PostMapping("/produtos/editar/salvar")
    public String doSalvarEditar(@ModelAttribute @Valid Produtos p, Errors errors, @RequestParam(name = "file", required = false) MultipartFile file, @RequestParam(name = "croppedImage", required = false) String croppedImage, RedirectAttributes redirectAttributes) throws IOException {
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

            if (file != null && !file.isEmpty()) {
                String fileExtension = getFileExtension(file);
                String imageName = generateUniqueFileName(fileExtension);
                String imagePath = fileStorageService.save2(file, imageName);
                p.setImagemUri(imagePath);
            } else {
                // Manter o valor existente do campo imagemUri
                Optional<Produtos> existingProduct = produtosService.findById(p.getId());
                if (existingProduct.isPresent()) {
                    p.setImagemUri(existingProduct.get().getImagemUri());
                }
            }

            if (croppedImage != null && !croppedImage.isEmpty()) {
                // A imagem foi cortada, salve-a diretamente
                byte[] imageData = decodeBase64Image(croppedImage);
                String fileExtension = getFileExtension(file);
                String imageName = generateUniqueFileName(fileExtension);
                String imagePath = fileStorageService.saveCroppedImage(imageData, imageName);
                p.setImagemUri(imagePath);
            }

            p.setData_atualizacao(Date.valueOf(LocalDate.now()));
            produtosService.editar(p);
            redirectAttributes.addFlashAttribute("mensagem", "Operação concluída com sucesso.");
            produtosService.salvarProduto(p);
            return "redirect:/meus-produtos";
        }
    }


    @RequestMapping(value = {"/produtos/catalogo"}, method = RequestMethod.GET)
    public String getCatalogo(Model model, Principal principal) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Produtos> produtosList = produtosService.listarProdutos();

        model.addAttribute("listarProdutos", produtosList);

        return "produtos/catalogo.html";
    }

    @GetMapping("/produtos/detalhes/{id}")
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

    @GetMapping(value = "/produtos/excluir/{id}")
    public String doExcluirProduto(@PathVariable long id, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("mensagem", "Produto excluido com sucesso.");

        produtosService.excluirProduto(id);
        return "redirect:/meus-produtos";
    }




    @GetMapping("/produtos/editar/{id}")
    public String getEditarPage(@PathVariable(name = "id") Long id, Model model){

        Optional<Produtos> produto = produtosService.findById(id);

        if (produto.isPresent()){
            model.addAttribute("produto", produto.get());
        }else{
            return "redirect:/meus-produtos";
        }

        return "produtos/editar";
    }
    @GetMapping("/produtos/editar-foto/{id}")
    public String getEditarFoto(@PathVariable(name = "id") Long id, Model model){

        Optional<Produtos> produto = produtosService.findById(id);

        if (produto.isPresent()){
            model.addAttribute("produto", produto.get());
        }else{
            return "redirect:/meus-produtos";
        }

        return "produtos/editar-imagem.html";
    }

    @GetMapping("/produtos/buscar")
    public String buscarProduto(@RequestParam("q") String query, Model model) {
        List<Produtos> produtosEncontrados = produtosService.buscarPorNome(query);

        if (produtosEncontrados.isEmpty()) {
            model.addAttribute("mensagem", "Ops! Não encontramos produtos com o nome " + query + " por favor, tente realizar outra busca!");
            return "produtos/catalogo.html";
        } else {
            model.addAttribute("produtos", produtosEncontrados);
            return "produtos/resultado-busca.html";
        }
    }

    // Método para excluir todos os produtos de um usuário
    @GetMapping("/produtos/excluir-todos/{id}")
    public String excluirTodosProdutos(@PathVariable Long id) {
        produtosService.excluirTodosProdutos(id);
        return "redirect:/meus-produtos";
    }

}
