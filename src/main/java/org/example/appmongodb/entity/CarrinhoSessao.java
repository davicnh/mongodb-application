package org.example.appmongodb.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoSessao {

    private String id;
    private String clienteId;
    private String sessaoId;
    private List<ItemCarrinho> itens;
    private LocalDateTime atualizadoEm;

    @Indexed(expireAfter = "0")
    private LocalDateTime expiraEm;
}
