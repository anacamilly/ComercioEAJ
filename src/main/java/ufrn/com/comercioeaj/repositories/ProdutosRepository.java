package ufrn.com.comercioeaj.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByVendedorIdAndDeletedIsNull(Long vendedorId);

    List<Produtos> findByTituloContainingIgnoreCaseAndDeletedIsNull(String titulo);
    List<Produtos> findByTituloAndCategoriaAndDeletedIsNull(String nome, String categoria);
    List<Produtos> findByCategoriaIgnoreCaseAndDeletedIsNull(String categoria);

    List<Produtos> findByDeletedIsNull();

    List<Produtos> findByVendedor_Id(Long usuarioId);

    @Query("SELECT p FROM Produtos p WHERE p.vendedor.id = :idVendedor AND p.deleted IS NULL")
    List<Produtos> findByVendedorIdPerfil(@Param("idVendedor") Long idVendedor);

    Produtos findByIdAndDeletedIsNull(Long id);

    @Query("SELECT p FROM Produtos p WHERE (p.titulo) = :titulo AND (p.categoria) = (:categoria)")
    List<Produtos> findByNomeAndCategoria(String titulo, String categoria);
}
