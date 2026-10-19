package org.example.appmongodb.entity;

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
    private TipoCliente tipo;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String cnpj;
    private LocalDateTime criadoEm = LocalDateTime.now();
    private List<Endereco> enderecos;

}
