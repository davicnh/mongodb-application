package org.example.appmongodb.controller;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Produto;
import org.example.appmongodb.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        List<Produto> produtos = service.getAll();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable String id) {
        Produto produto = service.getById(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto produtoNovo = service.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoNovo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable String id,@RequestBody Produto produto) {
        Produto atualizado = service.update(id, produto);
        return ResponseEntity.ok(atualizado);
    }
}
