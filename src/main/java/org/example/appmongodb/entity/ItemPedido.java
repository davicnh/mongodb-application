package org.example.appmongodb.entity;

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

    private UUID produtoId;
    private String nome;
    private String sku;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal subtotal;
}
