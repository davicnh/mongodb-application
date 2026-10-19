package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.dto.ProdutoDTO;
import org.example.appmongodb.entity.Produto;
import org.example.appmongodb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoDTO getById(UUID id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        return new ProdutoDTO(
                produto.getNome(), produto.getSku(), produto.getCategoria(),
                produto.getDescricao(), produto.getPreco(), produto.getEstoque(),
                produto.getAtributos(), produto.isAtivo()
        );
    }

    public List<ProdutoDTO> getAll() {
        return repository.findAll().stream()
                .map(produto -> new ProdutoDTO(
                        produto.getNome(), produto.getSku(), produto.getCategoria(),
                        produto.getDescricao(), produto.getPreco(), produto.getEstoque(),
                        produto.getAtributos(), produto.isAtivo()
                ))
                .toList();
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public ProdutoDTO save(ProdutoDTO dto) {
        boolean sku = repository.existsBySku(dto.sku());
        if (sku == true) {
            throw new RuntimeException("Já existe um produto com este SKU.");
        }

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setSku(dto.sku());
        produto.setCategoria(dto.categoria());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setAtivo(dto.ativo());
        produto.setAtributos(dto.atributos());

        Produto produtoSalvo = repository.save(produto);
        return new ProdutoDTO(produtoSalvo.getNome(),
                produto.getSku(),
                produtoSalvo.getCategoria(),
                produtoSalvo.getDescricao(),
                produtoSalvo.getPreco(),
                produto.getEstoque(),
                produtoSalvo.getAtributos(),
                produto.isAtivo());
    }

    public ProdutoDTO update(UUID id, ProdutoDTO dto) {
        Produto produto = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        produto.setNome(dto.nome());
        produto.setSku(dto.sku());
        produto.setCategoria(dto.categoria());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setAtivo(dto.ativo());
        produto.setAtributos(dto.atributos());

        Produto produtoSalvo = repository.save(produto);

        return new ProdutoDTO(
                produtoSalvo.getNome(),
                produto.getSku(),
                produtoSalvo.getCategoria(),
                produto.getDescricao(),
                produtoSalvo.getPreco(),
                produtoSalvo.getEstoque(),
                produtoSalvo.getAtributos(),
                produto.isAtivo()
        );
    }
}
