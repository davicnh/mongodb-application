package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Produto;
import org.example.appmongodb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public Produto getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    public List<Produto> getAll() {
        return repository.findAll();
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Produto save(Produto produto) {
        boolean skuExiste = repository.existsById(produto.getSku());

        if (skuExiste) {
            throw new RuntimeException("Já existe um produto com esse sku.");
        }
        return repository.save(produto);
    }

    public Produto update(String id, Produto produtoAtualizado) {
        Produto produto = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        produto.setSku(produtoAtualizado.getSku());
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
