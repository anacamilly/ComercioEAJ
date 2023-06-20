package ufrn.com.comercioeaj.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    String email;
    String login;
    String senha;
    Date data_nascimento;
    Date data_cadastro;
    Boolean isVendedor;

    // 2 etapa - Vendedor
    String imagemUri;
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

    public Boolean getIsVendedor() {
        return isVendedor;
    }

    public void setIsVendedor(Boolean vendedor) {
        isVendedor = vendedor;
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