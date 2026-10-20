package org.example.appmongodb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Cliente;
import org.example.appmongodb.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    public List<Cliente> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable String id){
        Cliente cliente = service.findById(id);
        return cliente;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@Valid @RequestBody Cliente cliente){
        return service.save(cliente);
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable String id, @Valid @RequestBody Cliente cliente){
        return service.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        service.delete(id);
    }
}
