package org.example.appmongodb.controller;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.service.CarrinhoSessaoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinhosessao")
@RequiredArgsConstructor
public class CarrinhoSessaoController {

    private final CarrinhoSessaoService service;
}
