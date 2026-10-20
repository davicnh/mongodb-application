package org.example.appmongodb.entity;

import lombok.*;
import org.example.appmongodb.entity.enums.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoStatus {

    private StatusPedido status;
    private LocalDateTime data;
}
