package org.example.appmongodb.repository;

import org.example.appmongodb.entity.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PedidoRepository extends MongoRepository<Pedido, UUID> {
}
