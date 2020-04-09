package br.com.ronistone.xyinc.service;

import br.com.ronistone.xyinc.model.ObjectInstance;

import java.util.List;
import java.util.Optional;

public interface ObjectInstanceService {

    Optional<ObjectInstance> createObjectInstance(String schemaName, ObjectInstance  instanceAttributes);

    Optional<List<ObjectInstance>> findAllInstances(String schemaName);

    Optional<ObjectInstance> findById(String schemaName, String id);

    Optional<ObjectInstance> updateById(String schemaName, String id, ObjectInstance  instanceAttributes);

    void deleteById(String schemaName, String id);
}
