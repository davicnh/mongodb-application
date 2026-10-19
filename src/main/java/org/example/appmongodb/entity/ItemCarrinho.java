package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemCarrinho {

    @NotBlank
    private UUID prudutoid;

    @Min(1)
    private int quantidade;
}
