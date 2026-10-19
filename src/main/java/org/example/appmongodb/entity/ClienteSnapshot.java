package org.example.appmongodb.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteSnapshot {

    private String nome;
    private String documento;
    private String email;
}
