package org.example.appmongodb.entity;

import lombok.*;
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
    private List<ItemCarrinho> items;
    private LocalDateTime atualizadoEm;
    private Date expiraEm;
}
