package org.example.appmongodb.repository;
import org.example.appmongodb.entity.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProdutoRepository extends MongoRepository<Produto, String> {

    boolean existsBySku(String sku);
}
