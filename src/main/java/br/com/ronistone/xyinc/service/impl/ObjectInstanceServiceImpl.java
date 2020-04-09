package br.com.ronistone.xyinc.service.impl;

import br.com.ronistone.xyinc.model.Attributes;
import br.com.ronistone.xyinc.exception.InstanceNotFound;
import br.com.ronistone.xyinc.exception.ValidationAttributeException;
import br.com.ronistone.xyinc.model.AttributesTypes;
import br.com.ronistone.xyinc.model.ObjectInstance;
import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.model.builder.ObjectInstanceBuilder;
import br.com.ronistone.xyinc.repository.ObjectInstanceRepository;
import br.com.ronistone.xyinc.service.ObjectInstanceService;
import br.com.ronistone.xyinc.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ObjectInstanceServiceImpl implements ObjectInstanceService {

    private final ObjectInstanceRepository objectInstanceRepository;
    private final SchemaService schemaService;

    @Autowired
    public ObjectInstanceServiceImpl(ObjectInstanceRepository objectInstanceRepository, SchemaService schemaService) {
        this.objectInstanceRepository = objectInstanceRepository;
        this.schemaService = schemaService;
    }


    @Override
    public Optional<ObjectInstance> createObjectInstance(String schemaName, ObjectInstance newObjectInstance) {
        Optional<Schema> schema = schemaService.findByName(schemaName);

        return schema.flatMap(value -> createNewObjectInstance(value, newObjectInstance));

    }

    @Override
    public Optional<ObjectInstance> updateById(String schemaName, String id, ObjectInstance  instanceAttributes) {

        Optional<ObjectInstance> oldObjectInstance = objectInstanceRepository.findBySchemaAndId(schemaName, id);
        Optional<Schema> schema = schemaService.findByName(schemaName);

        if(oldObjectInstance.isPresent() && schema.isPresent()) {
            return validateAndSaveObjectInstance(schema.get(), instanceAttributes, oldObjectInstance.get().getId());
        }

        return Optional.empty();
    }

    private Optional<ObjectInstance> createNewObjectInstance(Schema schema, ObjectInstance newObjectInstance) {
        return validateAndSaveObjectInstance(schema, newObjectInstance, null);
    }

    private Optional<ObjectInstance> validateAndSaveObjectInstance(Schema schema, ObjectInstance newInstanceObject, String oldId) {
        validateSchema(schema, newInstanceObject.getAttributes());

        ObjectInstance objectInstance = ObjectInstanceBuilder.create()
                .withSchema(schema.getName())
                .withId(oldId)
                .withAttributes(newInstanceObject.getAttributes())
                .build();

        return Optional.of(objectInstanceRepository.save(objectInstance));
    }

    @Override
    public Optional<List<ObjectInstance>> findAllInstances(String schemaName) {

        return objectInstanceRepository.findBySchema(schemaName);

    }

    @Override
    public Optional<ObjectInstance> findById(String schemaName, String id) {
        return objectInstanceRepository.findBySchemaAndId(schemaName, id);
    }

    @Override
    public void deleteById(String schemaName, String id) {
        Optional<ObjectInstance> objectInstance = objectInstanceRepository.findBySchemaAndId(schemaName, id);

        if(objectInstance.isPresent()) {
            objectInstanceRepository.deleteBySchemaAndId(schemaName, id);
        } else {
            throw new InstanceNotFound(schemaName + " with " + id + " not found" );
        }
    }

    private void validateSchema(Schema schema, Attributes attributes) {
        Attributes schemaAttributes = schema.getAttributes();

        attributes.entrySet()
                .forEach(entry -> validateAttribute(schemaAttributes, entry));
    }
    private void validateAttribute(Attributes schemaAttributes, Map.Entry<String, Object> entry) {
        String type = (String) schemaAttributes.get(entry.getKey());
        if(type != null) {
            validateTypes(entry, type);
        } else {
            throw new ValidationAttributeException("Attribute " + entry.getKey() + " does not exist");
        }
    }

    private void validateTypes(Map.Entry<String, Object> entry, String type) {
        for(AttributesTypes typeSupported: AttributesTypes.values()){
            if(isTypeSupported(type, typeSupported)) {
                validateTypeCorrect(entry, type, typeSupported);
                break;
            }
        }
        // TODO validar recursivamente lista e object...
    }

    private boolean isTypeSupported(String type, AttributesTypes typeSupported) {
        return type.toUpperCase().startsWith(typeSupported.name());
    }

    private void validateTypeCorrect(Map.Entry<String, Object> entry, String type, AttributesTypes typeSupported) {
        if(!typeSupported.getType().isAssignableFrom(entry.getValue().getClass())){
            throw new ValidationAttributeException(entry.getKey() + " must been " + type);
        }
    }

    ObjectInstanceRepository getObjectInstanceRepository() {
        return objectInstanceRepository;
    }
}
