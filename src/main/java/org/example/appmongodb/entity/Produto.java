package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Map;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    private String id;

    @NotBlank
    private String nome;

    @NotBlank
    @Indexed(unique = true)
    private String sku;

    @NotBlank
    private String categoria;

    private String descricao;

    @NotNull
    @Positive
    private BigDecimal precoAtual;

    @Min(0)
    private int estoque;
    private Map<String, Object> atributos;
    private boolean ativo;
}
