package org.example.appmongodb.repository;

import org.example.appmongodb.entity.CarrinhoSessao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CarrinhoSessaoRepository extends MongoRepository<CarrinhoSessao, UUID> {
}
