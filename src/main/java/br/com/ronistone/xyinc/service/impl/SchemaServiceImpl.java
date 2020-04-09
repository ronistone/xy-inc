package br.com.ronistone.xyinc.service.impl;

import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.repository.SchemaRepository;
import br.com.ronistone.xyinc.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    private SchemaRepository schemaRepository;

    @Override
    public Optional<Schema> findById(String id) {
        return schemaRepository.findById(id);
    }

    @Override
    public Schema create(Schema schema){
        return schemaRepository.save(schema);
    }

    @Override
    public List<Schema> findAll() {
        return schemaRepository.findAll();
    }

    @Override
    public Optional<Schema> findByName(String name) {
        return schemaRepository.findByName(name);
    }


}
