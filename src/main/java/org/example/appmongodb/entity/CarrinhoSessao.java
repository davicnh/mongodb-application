package org.example.appmongodb.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoSessao {

    private UUID id;
    private UUID clienteId;
    private UUID sessaoId;
    private List<ItemCarrinho> items;
    private LocalDateTime atualizadoEm;
    private Date expiraEm;
}
