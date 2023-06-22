package ufrn.com.comercioeaj.services;

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

    public Produtos editar(Produtos p){

        return repository.saveAndFlush(p);
    }

    public Optional<Produtos> findById(Long id){

        return repository.findById(id);
    }
    public List<Produtos> buscarPorNome(String nome){
        return repository.findByTituloContainingIgnoreCase(nome);
    }


}