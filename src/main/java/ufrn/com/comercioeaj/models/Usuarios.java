package ufrn.com.comercioeaj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ufrn.com.comercioeaj.errors.ApiMessages;

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

    @NotBlank(message = ApiMessages.ERRO_NOME)
    String nome;

    @NotBlank(message = ApiMessages.ERRO_NOME_SOCIAL)
    String nomeSocial;

    @NotBlank(message = ApiMessages.ERRO_EMAIL)
    @Column(unique=true)
    String email;

    @NotBlank(message = ApiMessages.ERRO_LOGIN)
    @Column(unique=true)
    String login;

    @NotBlank(message = ApiMessages.ERRO_SENHA)
    String senha;

    @NotNull(message = ApiMessages.ERRO_DATA_NASCIMENTO)
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
