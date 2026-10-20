package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @NotBlank
    private String produtoId;
    private String nome;
    private String sku;
    private BigDecimal precoUnitario;

    @Min(1)
    private int quantidade;

    private BigDecimal subtotal;
}
