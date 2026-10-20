package org.example.appmongodb.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.example.appmongodb.entity.enums.StatusPedido;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    private String id;

    @NotBlank
    private String clienteId;


    private ClienteSnapshot clienteSnapshot;

    @NotEmpty
    @Valid
    private List<ItemPedido> itens;

    @Valid
    private Endereco enderecoEntrega;
    private BigDecimal total;
    private StatusPedido status;
    private List<HistoricoStatus> historicoStatus;
    private LocalDateTime criadoEm;
}
