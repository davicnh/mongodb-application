package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @NotBlank
    private UUID produtoId;
    private String nome;
    private String sku;
    private BigDecimal preco;

    @Min(1)
    private Integer quantidade;

    private BigDecimal subtotal;
}
