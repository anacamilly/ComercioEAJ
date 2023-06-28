package ufrn.com.comercioeaj.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuarios implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nome;

    String nomeSocial;

    @Column(unique=true)
    String email;
    @Column(unique=true)
    String login;
    String senha;
    Date dataNascimento;
    Date dataCadastro;
    Boolean isVendedor;
    String imagemUri;


    // 2 etapa - Vendedor
    String razao;
    String biografia;
    String whatsapp;
    String instagram;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.isVendedor){
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_VEND"));
        }else{
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMP"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
