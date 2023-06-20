package ufrn.com.comercioeaj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.UsuariosRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ComercioEajApplication implements WebMvcConfigurer {

        @Bean
        CommandLineRunner commandLineRunner(UsuariosRepository usuarioRepository, PasswordEncoder encoder) {
            return args -> {

                List<Usuarios> users = Stream.of(
                        new Usuarios(1L, "user", "camilly", "admin", encoder.encode("admin"), true)
                        ).collect(Collectors.toList());

                for (var e : users) {
                    System.out.println(e);
                }
                usuarioRepository.saveAll(users);
            };
        }


        public static void main(String[] args) {
            SpringApplication.run(ComercioEajApplication.class, args);
        }

    }
