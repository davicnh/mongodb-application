package org.example.appmongodb.repository;

import org.example.appmongodb.entity.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

    boolean existsByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByCnpj(String cnpj);

}
