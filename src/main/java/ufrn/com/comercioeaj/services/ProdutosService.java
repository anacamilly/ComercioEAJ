package ufrn.com.comercioeaj.services;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.ProdutosRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutosService {
    private ProdutosRepository repository;

    public ProdutosService(ProdutosRepository produtosRepository) {
        this.repository = produtosRepository;
    }




    public void salvarProduto(Produtos p) {
        repository.save(p);
    }

    public List<Produtos> listarProdutos() {
        return repository.findByDeletedIsNull();
    }

    public List<Produtos> listarProdutosVendedor(Long id) {
        return repository.findByVendedorIdPerfil(id);
    }


    public List<Produtos> buscarProdutosPorUsuarioId(Long usuarioId) {
        return repository.findByVendedor_Id(usuarioId);
    }


    public Optional<Produtos> buscarProdutoPorId(Long id) {
        return repository.findById(id);
    }

    public void excluirProduto(Long id) {
        Produtos produtos = repository.findByIdAndDeletedIsNull(id);
        if(produtos != null){
            produtos.setDeleted(LocalDate.now());
            repository.save(produtos);
        }
    }

    public List<Produtos> buscarProdutosPorVendedor(Long vendedorId) {
        return repository.findByVendedorIdAndDeletedIsNull(vendedorId);
    }

    public void excluirTodosProdutos(Long usuarioId) {
        // Lógica para excluir todos os produtos associados ao usuário com o ID fornecido
        List<Produtos> produtos = repository.findByVendedor_Id(usuarioId);
        repository.deleteAll(produtos);
    }

    public Produtos editar(Produtos p){

        return repository.saveAndFlush(p);
    }

    public Optional<Produtos> findById(Long id){

        return repository.findById(id);
    }
    public List<Produtos> buscarPorNome(String nome) {

        return repository.findByTituloContainingIgnoreCase(nome);
    }

    public List<Produtos> buscarPorNomeECategoria(String nome, String categoria) {
        return repository.findByNomeAndCategoria(nome, categoria);
    }

    public List<Produtos> buscarPorCategoria(String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }




}