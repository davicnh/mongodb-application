package org.example.appmongodb.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoStatus {

    private HistoricoStatus status;
    private LocalDate data;
    private String observacao;
}
