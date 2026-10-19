package org.example.appmongodb.repository;

import org.example.appmongodb.entity.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ClienteRepository extends MongoRepository<Cliente, UUID> {
}
