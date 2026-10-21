package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.*;
import org.example.appmongodb.repository.CarrinhoSessaoRepository;
import org.example.appmongodb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CarrinhoSessaoService {

    private static final int VALIDADE_MINUTOS = 30;

    private final CarrinhoSessaoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final PedidoService pedidoService;

    public CarrinhoSessao findById(String id) {
        CarrinhoSessao carrinho = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Carrinho não encontrado"));

        if (carrinho.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new NoSuchElementException("Carrinho expirado");
        }
        return carrinho;
    }

    public CarrinhoSessao criar(CarrinhoSessao carrinho) {
        carrinho.setId(null);
        carrinho.setItens(new ArrayList<>());
        renovarValidade(carrinho);
        return repository.save(carrinho);
    }

    public CarrinhoSessao adicionarItem(String id, ItemCarrinho novo) {
        CarrinhoSessao carrinho = findById(id);

        Produto produto = produtoRepository.findById(novo.getProdutoId())
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + novo.getProdutoId()));

        if (!produto.isAtivo()) {
            throw new IllegalArgumentException("Produto inativo: " + produto.getNome());
        }

        ItemCarrinho existente = null;
        for (ItemCarrinho item : carrinho.getItens()) {
            if (item.getProdutoId().equals(novo.getProdutoId())) {
                existente = item;
                break;
            }
        }

        int quantidadeFinal = novo.getQuantidade();
        if (existente != null) {
            quantidadeFinal += existente.getQuantidade();
        }

        if (produto.getEstoque() < quantidadeFinal) {
            throw new IllegalArgumentException("Estoque insuficiente para: " + produto.getNome());
        }

        if (existente != null) {
            existente.setQuantidade(quantidadeFinal);
        } else {
            carrinho.getItens().add(novo);
        }

        renovarValidade(carrinho);
        return repository.save(carrinho);
    }

    public CarrinhoSessao removerItem(String id, String produtoId) {
        CarrinhoSessao carrinho = findById(id);

        boolean removido = carrinho.getItens().removeIf(item -> item.getProdutoId().equals(produtoId));
        if (!removido) {
            throw new NoSuchElementException("Item não está no carrinho");
        }

        renovarValidade(carrinho);
        return repository.save(carrinho);
    }

    public void apagar(String id) {
        CarrinhoSessao carrinho = findById(id);
        repository.delete(carrinho);
    }

    public Pedido finalizar(String id) {
        CarrinhoSessao carrinho = findById(id);

        if (carrinho.getClienteId() == null || carrinho.getClienteId().isBlank()) {
            throw new IllegalArgumentException("Informe o cliente do carrinho para finalizar");
        }
        if (carrinho.getItens().isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio");
        }

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemCarrinho item : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProdutoId(item.getProdutoId());
            itemPedido.setQuantidade(item.getQuantidade());
            itens.add(itemPedido);
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(carrinho.getClienteId());
        pedido.setItens(itens);


        Pedido criado = pedidoService.criar(pedido);

        repository.delete(carrinho);
        return criado;
    }

    private void renovarValidade(CarrinhoSessao carrinho) {
        LocalDateTime agora = LocalDateTime.now();
        carrinho.setAtualizadoEm(agora);
        carrinho.setExpiraEm(agora.plusMinutes(VALIDADE_MINUTOS));
    }
}


