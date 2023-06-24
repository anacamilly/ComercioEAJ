package ufrn.com.comercioeaj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ufrn.com.comercioeaj.models.Produtos;
import ufrn.com.comercioeaj.models.Usuarios;
import ufrn.com.comercioeaj.repositories.ProdutosRepository;
import ufrn.com.comercioeaj.repositories.UsuariosRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class ComercioEajApplication implements WebMvcConfigurer {

        @Bean
        CommandLineRunner commandLineRunner(UsuariosRepository usuarioRepository, ProdutosRepository produtosRepository, PasswordEncoder encoder) {
            return args -> {

                List<Usuarios> users = Stream.of(
                        new Usuarios(1L, "user", "camilly@teste.com", "admin", encoder.encode("admin"),
                                Date.valueOf(LocalDate.parse("2003-09-25", DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                                Date.valueOf(LocalDate.parse("2003-09-25", DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                                true,
                                "livro.png", "Lojinha", "Lojinha ", "84 9999-9999", "@lojinha")
                ).collect(Collectors.toList());

                for (var e : users) {
                    System.out.println(e);
                }
                usuarioRepository.saveAll(users);

                /*List<Produtos> produtos = Stream.of(
                        new Produtos(1L, "livro.png", "Título", "Descrição", "Categoria", 10.99f, true,
                                Date.valueOf("10/09/2022"), Date.valueOf(""), null, 1L)
                ).collect(Collectors.toList());

                produtosRepository.saveAll(produtos);*/
            };
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // Register resource handler for images
            registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
        }

        public static void main(String[] args) {
            SpringApplication.run(ComercioEajApplication.class, args);
        }

    }
