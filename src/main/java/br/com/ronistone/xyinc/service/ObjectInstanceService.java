package br.com.ronistone.xyinc.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ObjectInstanceService {

    Optional<Map<String, Object>> create(String schemaName, Map<String,Object>  instanceAttributes);

    Optional<List<Map<String, Object>>> findAll(String schemaName);

    Optional<Map<String, Object>> findById(String schemaName, String id);

    Optional<Map<String, Object>> updateById(String schemaName, String id, Map<String,Object>  instanceAttributes);

    void deleteById(String schemaName, String id);
}
