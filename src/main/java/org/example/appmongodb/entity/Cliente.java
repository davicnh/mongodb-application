package org.example.appmongodb.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.appmongodb.entity.enums.TipoCliente;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cliente {

    private UUID id;

    @NotNull
    private TipoCliente tipo;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nome;

    @NotBlank
    @Email
    private String email;

    private String telefone;
    private String cpf;
    private String cnpj;
    private LocalDateTime criadoEm = LocalDateTime.now();

    @NotEmpty
    @Valid
    private List<Endereco> enderecos;

}
