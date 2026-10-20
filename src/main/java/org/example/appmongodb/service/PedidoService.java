package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.*;
import org.example.appmongodb.entity.enums.StatusPedido;
import org.example.appmongodb.entity.enums.TipoCliente;
import org.example.appmongodb.repository.ClienteRepository;
import org.example.appmongodb.repository.PedidoRepository;
import org.example.appmongodb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public Pedido findById(String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));
    }

    public List<Pedido> findAll(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            return pedidoRepository.findAll();
        }
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido criar(Pedido pedido) {
        pedido.setId(null);
        pedido.setCriadoEm(LocalDateTime.now());
        pedido.setStatus(StatusPedido.CRIADO);

        Cliente cliente = clienteRepository.findById(pedido.getClienteId())
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
        String documento = cliente.getTipo() == TipoCliente.B2C ? cliente.getCpf() : cliente.getCnpj();
        pedido.setClienteSnapshot(new ClienteSnapshot(cliente.getNome(), documento, cliente.getEmail()));

        if (pedido.getEnderecoEntrega() == null) {
            pedido.setEnderecoEntrega(cliente.getEnderecos().get(0));
        }

        List<Produto> produtos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + item.getProdutoId()));

            if (!produto.isAtivo()) {
                throw new IllegalArgumentException("Produto inativo: " + produto.getNome());
            }
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para: " + produto.getNome());
            }

            item.setNome(produto.getNome());
            item.setSku(produto.getSku());
            item.setPrecoUnitario(produto.getPrecoAtual());
            item.setSubtotal(produto.getPrecoAtual().multiply(BigDecimal.valueOf(item.getQuantidade())));

            total = total.add(item.getSubtotal());
            produtos.add(produto);
        }
        pedido.setTotal(total);

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            produto.setEstoque(produto.getEstoque() - pedido.getItens().get(i).getQuantidade());
        }
        produtoRepository.saveAll(produtos);


        pedido.setHistoricoStatus(new ArrayList<>());
        pedido.getHistoricoStatus().add(new HistoricoStatus(StatusPedido.CRIADO, LocalDateTime.now()));

        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(String id, StatusPedido novoStatus) {
        Pedido pedido = findById(id);

        if (novoStatus == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("Para cancelar, use o endpoint de cancelamento");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalArgumentException("Pedido " + pedido.getStatus() + " não pode mais mudar de status");
        }
        if (novoStatus.ordinal() <= pedido.getStatus().ordinal()) {
            throw new IllegalArgumentException("O status só pode avançar");
        }

        pedido.setStatus(novoStatus);
        pedido.getHistoricoStatus().add(new HistoricoStatus(novoStatus, LocalDateTime.now()));
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelar(String id) {
        Pedido pedido = findById(id);

        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalArgumentException("Pedido " + pedido.getStatus() + " não pode ser cancelado");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + item.getProdutoId()));
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.getHistoricoStatus().add(new HistoricoStatus(StatusPedido.CANCELADO, LocalDateTime.now()));
        return pedidoRepository.save(pedido);
    }
}

