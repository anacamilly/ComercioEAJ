package ufrn.com.comercioeaj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.UsuariosRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuariosService implements UserDetailsService {

    UsuariosRepository repository;
    BCryptPasswordEncoder encoder;



    public UsuariosService(UsuariosRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void create(Usuarios u){
        u.setSenha(encoder.encode(u.getSenha()));
        this.repository.save(u);
    }

    public Optional<Usuarios> findById(Long id) {
        return repository.findById(String.valueOf(id));
    }

    public Usuarios atualizar(Usuarios u){

        return repository.saveAndFlush(u);
    }

    public Optional<Usuarios> findByEmail(String email) {
        return Optional.ofNullable(repository.findByEmail(email));
    }

    public Usuarios editar(Usuarios u){
        return repository.saveAndFlush(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuarios> user = repository.findUsuarioByLogin(username);
        if (user.isPresent()){
            return user.get();
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public List<Usuarios> buscarVendedor(String nome) {

        return repository.findByNomeContainingIgnoreCaseAndIsVendedorIsTrue(nome);
    }

}