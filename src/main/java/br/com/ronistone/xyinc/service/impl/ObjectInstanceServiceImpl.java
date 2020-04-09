package br.com.ronistone.xyinc.service.impl;

import br.com.ronistone.xyinc.exception.CantDeleteException;
import br.com.ronistone.xyinc.exception.ValidationAttributeException;
import br.com.ronistone.xyinc.model.AttributesTypes;
import br.com.ronistone.xyinc.model.ObjectInstance;
import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.model.builder.ObjectInstanceBuilder;
import br.com.ronistone.xyinc.repository.ObjectInstanceRepository;
import br.com.ronistone.xyinc.service.ObjectInstanceService;
import br.com.ronistone.xyinc.service.SchemaService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectInstanceServiceImpl implements ObjectInstanceService {

    private final ObjectInstanceRepository repository;
    private final SchemaService schemaService;

    public ObjectInstanceServiceImpl(ObjectInstanceRepository repository, SchemaService schemaService) {
        this.repository = repository;
        this.schemaService = schemaService;
    }


    @Override
    public Optional<Map<String, Object>> create(String schemaName, Map<String,Object> instanceAttributes) {
        var schema = schemaService.findByName(schemaName);

        if(schema.isPresent()) {
            validateSchema(schema.get(), instanceAttributes);
            ObjectInstance objectInstance = ObjectInstanceBuilder.create()
                    .schema(schemaName)
                    .attributes(instanceAttributes)
                    .build();
            return getInstanceAttributesOptional(repository.save(objectInstance));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Map<String, Object>> updateById(String schemaName, String id, Map<String,Object>  instanceAttributes) {

        var oldObjectInstance = repository.findBySchemaAndId(schemaName, id);
        var schema = schemaService.findByName(schemaName);

        if(oldObjectInstance.isPresent() && schema.isPresent()) {
            validateSchema(schema.get(), instanceAttributes);
            ObjectInstance objectInstance = ObjectInstanceBuilder.create()
                    .schema(schemaName)
                    .id(oldObjectInstance.get().getId())
                    .attributes(instanceAttributes)
                    .build();
            return getInstanceAttributesOptional(repository.save(objectInstance));
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<Map<String, Object>>> findAll(String schemaName) {
        var response = repository.findBySchema(schemaName);

        if(response.isPresent() && !CollectionUtils.isEmpty(response.get())) {
            return response.map(instances -> instances
                    .stream()
                    .map(this::getInstanceAttributes)
                    .collect(Collectors.toList())
            );
        }
        return Optional.empty();

    }

    @Override
    public Optional<Map<String, Object>> findById(String schemaName, String id) {
        return getInstanceAttributesOptional(repository.findBySchemaAndId(schemaName, id));
    }

    @Override
    public void deleteById(String schemaName, String id) {
        var oldObjectInstance = repository.findBySchemaAndId(schemaName, id);

        if(oldObjectInstance.isPresent()) {
            repository.deleteBySchemaAndId(schemaName, id);
        } else {
            throw new CantDeleteException(schemaName + " with " + id + " not found" );
        }
    }



    private Optional<Map<String, Object>> getInstanceAttributesOptional(ObjectInstance objectInstance) {
        return Optional.ofNullable(getInstanceAttributes(objectInstance));
    }

    private Optional<Map<String, Object>> getInstanceAttributesOptional(Optional<ObjectInstance> objectInstance) {
        if(objectInstance.isPresent()){
            return getInstanceAttributesOptional(objectInstance.get());
        }
        return Optional.empty();
    }

    private Map<String, Object> getInstanceAttributes(ObjectInstance objectInstance) {
        if(objectInstance != null) {
            Map attributes = objectInstance.getAttributes();
            if(attributes == null){
                attributes = new HashMap<>();
            }
            attributes.put("id", objectInstance.getId());
            return attributes;
        }
        return null;
    }

    private void validateSchema(Schema schema, Map<String, Object> attributes) {
        Map schemaAttributes = schema.getAttributes();

        attributes.entrySet()
                .forEach(
                        entry -> validateAttribute(schemaAttributes, entry)
                );
    }
    private void validateAttribute(Map<String, String> schemaAttributes, Map.Entry<String, Object> entry) {
        String type = schemaAttributes.get(entry.getKey());
        if(type != null) {
            for(AttributesTypes typeSupported: AttributesTypes.values()){
                if(type.toUpperCase().startsWith(typeSupported.name())) {
                    validateTypeCorrect(entry, type, typeSupported);
                    // TODO validar recursivamente lista e object...
                    break;
                }
            }
        } else {
            throw new ValidationAttributeException("Attribute " + entry.getKey() + " does not exist");
        }
    }

    private void validateTypeCorrect(Map.Entry<String, Object> entry, String type, AttributesTypes typeSupported) {
        if(!typeSupported.getType().isAssignableFrom(entry.getValue().getClass())){
            throw new ValidationAttributeException(entry.getKey() + " must been " + type);
        }
    }

    ObjectInstanceRepository getRepository() {
        return repository;
    }
}
