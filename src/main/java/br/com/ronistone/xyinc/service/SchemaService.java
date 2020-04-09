package br.com.ronistone.xyinc.service;

import br.com.ronistone.xyinc.model.Schema;

import java.util.List;
import java.util.Optional;

public interface SchemaService {
    Optional<Schema> findById(String id);

    Schema create(Schema schema);

    List<Schema> findAll();

    Optional<Schema> findByName(String name);
}
