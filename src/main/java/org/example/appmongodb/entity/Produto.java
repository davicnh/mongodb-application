package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank
    private String nome;

    @NotBlank
    private String sku;

    @NotBlank
    private String categoria;

    private String descricao;

    @NotNull
    @Positive
    private BigDecimal preco;

    @Min(0)
    private Integer estoque;
    private Map<String, Object> atributos;
    private boolean ativo;
}
