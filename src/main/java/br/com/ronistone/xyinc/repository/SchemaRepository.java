package br.com.ronistone.xyinc.repository;

import br.com.ronistone.xyinc.model.Schema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchemaRepository extends MongoRepository<Schema, String> {

    Optional<Schema> findByName(String name);

}
