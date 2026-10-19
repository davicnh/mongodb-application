package org.example.appmongodb.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    private UUID id;
    private String nome;
    private String sk;
    private String categoria;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Map<String, Object> atributos;
    private boolean ativo;
}
