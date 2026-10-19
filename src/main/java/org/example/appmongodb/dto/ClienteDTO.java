package org.example.appmongodb.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.appmongodb.entity.Endereco;
import org.example.appmongodb.entity.enums.TipoCliente;

import java.time.LocalDateTime;
import java.util.List;

public record ClienteDTO(

        @NotNull
        TipoCliente tipo,

        @NotBlank
        @Size(min = 3, max = 120)
        String nome,

        @NotBlank
        @Email
        String email,

        String telefone,
        String cpf,
        String cnpj,
        LocalDateTime criadoEm,

        @NotEmpty
        @Valid
        List<Endereco> enderecos
) {
}
