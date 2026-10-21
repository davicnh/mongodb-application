package org.example.appmongodb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Pedido;
import org.example.appmongodb.entity.enums.StatusPedido;
import org.example.appmongodb.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @GetMapping("/{id}")
    public Pedido findByid(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping
    public List<Pedido> findAll(@RequestParam(required = false) String clienteId) {
        return service.findAll(clienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido save(@Valid @RequestBody Pedido pedido) {
        return service.criar(pedido);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.cancelar(id);
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable String id, @RequestParam StatusPedido status) {
        return service.atualizarStatus(id, status);
    }

}
