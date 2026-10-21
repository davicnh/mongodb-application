package org.example.appmongodb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.CarrinhoSessao;
import org.example.appmongodb.entity.ItemCarrinho;
import org.example.appmongodb.entity.Pedido;
import org.example.appmongodb.service.CarrinhoSessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/carrinhosessao")
@RequiredArgsConstructor
public class CarrinhoSessaoController {

    private final CarrinhoSessaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoSessao criar(@RequestBody CarrinhoSessao carrinho) {
        return service.criar(carrinho);
    }

    @GetMapping("/{id}")
    public CarrinhoSessao findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping("/{id}/itens")
    public CarrinhoSessao adicionarItem(@PathVariable String id, @Valid @RequestBody ItemCarrinho item) {
        return service.adicionarItem(id, item);
    }

    @DeleteMapping("/{id}/itens/{produtoId}")
    public CarrinhoSessao removerItem(@PathVariable String id, @PathVariable String produtoId) {
        return service.removerItem(id, produtoId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable String id) {
        service.apagar(id);
    }

    @PostMapping("/{id}/finalizar")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido finalizar(@PathVariable String id) {
        return service.finalizar(id);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String naoEncontrado(NoSuchElementException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String requisicaoInvalida(IllegalArgumentException e) {
        return e.getMessage();
    }
}
