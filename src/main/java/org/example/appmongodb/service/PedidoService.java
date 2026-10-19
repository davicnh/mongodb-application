package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.repository.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
}
