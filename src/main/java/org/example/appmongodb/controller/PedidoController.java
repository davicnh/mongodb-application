package org.example.appmongodb.controller;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Pedido;
import org.example.appmongodb.repository.PedidoRepository;
import org.example.appmongodb.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

}
