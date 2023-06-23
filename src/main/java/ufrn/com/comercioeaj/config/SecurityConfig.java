package ufrn.com.comercioeaj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/meus-produtos", "/produtos", "/produtos/cadastro", "/produtos/editar-foto/{id}", "/produtos/editar/{id}", "/produtos/excluir/{id}", "/produtos/salvar").hasRole("VEND");
                    //auth.requestMatchers("/").hasRole("COMP");
                    auth.anyRequest().permitAll();
                })
                .formLogin(login -> login.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutUrl("/logout"))
                .build();
        //return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()).build();
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
