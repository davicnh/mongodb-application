package org.example.appmongodb.entity;

import lombok.*;
import org.example.appmongodb.entity.enums.StatusPedido;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    private UUID id;
    private UUID clienteid;
    private ClienteSnapshot clienteSnapshot;
    private List<ItemPedido> items;
    private Endereco enderecoEntrega;
    private BigDecimal total;
    private StatusPedido status;
    private List<HistoricoStatus> historicoStatus;
    private LocalDate criadoEm;
}
