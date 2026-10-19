package org.example.appmongodb.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemCarrinho {

    private UUID prudutoid;
    private int quantidade;
}
