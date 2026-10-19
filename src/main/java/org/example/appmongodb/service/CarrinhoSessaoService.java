package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.CarrinhoSessao;
import org.example.appmongodb.repository.CarrinhoSessaoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarrinhoSessaoService {

    private final CarrinhoSessaoRepository repository;
}
