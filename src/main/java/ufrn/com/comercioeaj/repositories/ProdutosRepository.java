package ufrn.com.comercioeaj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
    List<Produtos> findByDeletedIsNull();
    Produtos findByIdAndDeletedIsNull(Long id);
}
