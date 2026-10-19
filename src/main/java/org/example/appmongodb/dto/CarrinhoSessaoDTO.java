package org.example.appmongodb.dto;

import org.example.appmongodb.entity.ItemCarrinho;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CarrinhoSessaoDTO(

        UUID clienteId,
        UUID sessaoId,
        List<ItemCarrinho> items,
        LocalDateTime atualizadoEm,
        Date expiraEm
) {
}
