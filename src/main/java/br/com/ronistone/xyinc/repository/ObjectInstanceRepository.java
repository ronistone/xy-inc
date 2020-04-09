package br.com.ronistone.xyinc.repository;

import br.com.ronistone.xyinc.model.ObjectInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectInstanceRepository extends MongoRepository<ObjectInstance, String> {

    Optional<List<ObjectInstance>> findBySchema(String schema);

    Optional<ObjectInstance> findBySchemaAndId(String schema, String id);

    Optional<ObjectInstance> deleteBySchemaAndId(String schema, String id);

}
