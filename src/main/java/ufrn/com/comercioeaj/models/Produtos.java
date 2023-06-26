package ufrn.com.comercioeaj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String imagemUri;
    String titulo;
    String descricao;
    String categoria;
    float preco;
    boolean disponibilidade;
    Date data_cadastro;
    Date data_atualizacao;

    @Column
    private LocalDate deleted;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuarios vendedor;

}
