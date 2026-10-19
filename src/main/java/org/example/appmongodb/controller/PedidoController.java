package org.example.appmongodb.controller;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.repository.PedidoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoRepository pedidoRepository;
}
