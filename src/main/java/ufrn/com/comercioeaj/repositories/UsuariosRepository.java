package ufrn.com.comercioeaj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.com.comercioeaj.models.Usuarios;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
    Optional<Usuarios> findUsuarioByLogin(String login);

}