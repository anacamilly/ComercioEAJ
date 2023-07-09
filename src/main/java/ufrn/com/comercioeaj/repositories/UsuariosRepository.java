package ufrn.com.comercioeaj.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
    Optional<Usuarios> findUsuarioByLogin(String login);

    Usuarios findByEmail(String email);
    @Transactional
    List<Usuarios> deleteUsuariosById( Long id);

    List<Usuarios> findByIsVendedorTrue();

    Optional<Usuarios> findByLogin(String login);
    List<Usuarios> findByNomeContainingIgnoreCaseAndIsVendedorIsTrue(String nome);


}