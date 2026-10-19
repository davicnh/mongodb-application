package org.example.appmongodb.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.appmongodb.entity.ClienteSnapshot;
import org.example.appmongodb.entity.Endereco;
import org.example.appmongodb.entity.HistoricoStatus;
import org.example.appmongodb.entity.ItemPedido;
import org.example.appmongodb.entity.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PedidoDTO(

        @NotBlank
        UUID clienteid,

        ClienteSnapshot clienteSnapshot,

        @NotEmpty
        @Valid
        List<ItemPedido>items,

        @NotNull
        @Valid
        Endereco enderecoEntrega,
        BigDecimal total,
        StatusPedido status,
        List<HistoricoStatus>historicoStatus,
        LocalDate criadoEm
) {
}
