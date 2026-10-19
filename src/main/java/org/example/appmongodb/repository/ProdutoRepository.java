package org.example.appmongodb.repository;

import org.example.appmongodb.entity.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProdutoRepository extends MongoRepository<Produto, UUID> {
}
