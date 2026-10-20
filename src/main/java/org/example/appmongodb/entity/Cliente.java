package org.example.appmongodb.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.appmongodb.entity.enums.TipoCliente;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cliente {

    @Id
    private String id;

    @NotNull
    private TipoCliente tipo;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nome;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;

    private String telefone;
    private String cpf;
    private String cnpj;
    private LocalDateTime criadoEm = LocalDateTime.now();

    @NotEmpty
    @Valid
    private List<Endereco> enderecos;

}
