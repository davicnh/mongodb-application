package org.example.appmongodb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemCarrinho {

    @NotBlank
    private String prudutoId;

    @Min(1)
    private int quantidade;
}
