package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Produto;
import org.example.appmongodb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Produto getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));
    }

    public List<Produto> getAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Produto save(Produto produto) {
        produto.setId(null);
        produto.setSku(produto.getSku().trim().toUpperCase());

        if (repository.existsBySku(produto.getSku())) {
            throw new IllegalArgumentException("Já existe um produto com o sku " + produto.getSku());
        }

        return repository.save(produto);
    }

    public Produto update(String id, Produto produtoAtualizado) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));

        String sku = produtoAtualizado.getSku().trim().toUpperCase();
        if (!produto.getSku().equals(sku) && repository.existsBySku(sku)) {
            throw new IllegalArgumentException("Já existe um produto com o sku " + sku);
        }

        produto.setSku(sku);
        produto.setNome(produtoAtualizado.getNome());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPrecoAtual(produtoAtualizado.getPrecoAtual());
        produto.setEstoque(produtoAtualizado.getEstoque());
        produto.setAtributos(produtoAtualizado.getAtributos());
        produto.setAtivo(produtoAtualizado.isAtivo());
        return repository.save(produto);
    }
}
