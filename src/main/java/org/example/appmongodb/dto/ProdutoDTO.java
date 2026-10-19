package org.example.appmongodb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Map;

public record ProdutoDTO(

        @NotBlank
        String nome,
        @NotBlank
        String sku,
        @NotBlank
        String categoria,
        String descricao,
        @NotNull
        @Positive
        BigDecimal preco,
        @Min(0)
        Integer estoque,
        Map<String, Object>atributos,
        boolean ativo
){
}
