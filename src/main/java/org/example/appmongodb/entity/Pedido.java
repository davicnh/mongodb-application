package org.example.appmongodb.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private UUID clienteid;


    private ClienteSnapshot clienteSnapshot;

    @NotEmpty
    @Valid
    private List<ItemPedido> items;

    @NotNull
    @Valid
    private Endereco enderecoEntrega;
    private BigDecimal total;
    private StatusPedido status;
    private List<HistoricoStatus> historicoStatus;
    private LocalDate criadoEm;
}
