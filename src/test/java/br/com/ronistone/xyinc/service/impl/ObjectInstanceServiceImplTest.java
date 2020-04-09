package br.com.ronistone.xyinc.service.impl;

import br.com.ronistone.xyinc.model.ObjectInstance;
import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.model.builder.ObjectInstanceBuilder;
import br.com.ronistone.xyinc.repository.ObjectInstanceRepository;
import br.com.ronistone.xyinc.service.SchemaService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;

public class ObjectInstanceServiceImplTest {


    public static final String SCHEMA_NAME = "XPTO";
    public static final String INSTANCE_ID = "ID-XPTO";
    public static final String SCHEMA_ID = "ID-XPTO";

    @Test
    void create_save_success() {

        ObjectInstanceServiceImpl service = setupCreateCenario(Optional.of(createSchema()));

        Optional result = service.create(SCHEMA_NAME, createAttributes());

        verify(service.getRepository(), times(1)).save(any(ObjectInstance.class));
        assertTrue("the instance must been created", result.isPresent());
    }

    @Test
    void create_attributesWithTypeWrong_fail() {
        try {
            ObjectInstanceServiceImpl service = setupCreateCenario(Optional.of(createSchema()));

            Optional result = service.create(SCHEMA_NAME, createBrokenAttributes());

            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("The message must be", "XZ must been OBJECT", e.getMessage());
        }
    }

    @Test
    void create_schemaNotFound_fail() {
        ObjectInstanceServiceImpl service = setupCreateCenario(Optional.empty());

        Optional result = service.create(SCHEMA_NAME, createAttributes());

        verify(service.getRepository(), times(0)).save(any(ObjectInstance.class));
        assertFalse("the instance must not be created", result.isPresent());
    }

    private ObjectInstanceServiceImpl setupCreateCenario(Optional schemaReturn) {
        ObjectInstanceRepository repository = mock( ObjectInstanceRepository.class );
        SchemaService schemaService = mock( SchemaService.class );
        ObjectInstanceServiceImpl service = new ObjectInstanceServiceImpl(repository, schemaService);

        when(schemaService.findByName(eq(SCHEMA_NAME))).thenReturn(
                schemaReturn
        );
        when(repository.save(any(ObjectInstance.class))).thenReturn(createObjectInstance());
        return service;
    }

    @Test
    void update_save_success() {

        ObjectInstanceServiceImpl service = setupUpdateCenario(Optional.of(createObjectInstance()), Optional.of(createSchema()));

        Optional result = service.updateById(SCHEMA_NAME, INSTANCE_ID, createAttributes());

        verify(service.getRepository(), times(1)).save(any(ObjectInstance.class));
        assertTrue("the instance must been created", result.isPresent());
    }

    @Test
    void update_schemaNotFound_fail() {
        ObjectInstanceServiceImpl service = setupUpdateCenario(Optional.empty(), Optional.empty());

        Optional result = service.updateById(SCHEMA_NAME, INSTANCE_ID, createAttributes());

        verify(service.getRepository(), times(0)).save(any(ObjectInstance.class));
        assertFalse("the instance must not be created", result.isPresent());
    }

    private ObjectInstanceServiceImpl setupUpdateCenario(Optional instanceReturn, Optional schema) {
        ObjectInstanceRepository repository = mock( ObjectInstanceRepository.class );
        SchemaService schemaService = mock( SchemaService.class );
        ObjectInstanceServiceImpl service = new ObjectInstanceServiceImpl(repository, schemaService);

        when(repository.findBySchemaAndId(eq(SCHEMA_NAME), eq(SCHEMA_ID))).thenReturn(
                instanceReturn
        );
        when(schemaService.findByName(eq(SCHEMA_NAME))).thenReturn(schema);
        when(repository.save(any(ObjectInstance.class))).thenReturn(createObjectInstance());
        return service;
    }

    @Test
    void findAll_save_success() {

        ObjectInstanceServiceImpl service = setupFindAllCenario(Optional.of(createInstanceList()));

        Optional result = service.findAll(SCHEMA_NAME);

        assertTrue("the instance must been found", result.isPresent());
    }

    @Test
    void findAll_instancesNotFound_fail() {
        ObjectInstanceServiceImpl service = setupFindAllCenario(Optional.empty());

        Optional result = service.findAll(SCHEMA_NAME);

        assertFalse("the instance must not be found", result.isPresent());
    }

    private ObjectInstanceServiceImpl setupFindAllCenario(Optional instanceReturn) {
        ObjectInstanceRepository repository = mock( ObjectInstanceRepository.class );
        ObjectInstanceServiceImpl service = new ObjectInstanceServiceImpl(repository, null);

        when(repository.findBySchema(eq(SCHEMA_NAME))).thenReturn(
                instanceReturn
        );
        return service;
    }

    @Test
    void delete_instanceFound_success() {

        ObjectInstanceServiceImpl service = setupDeleteCenario(Optional.of(createObjectInstance()));

        service.deleteById(SCHEMA_NAME, INSTANCE_ID);

        verify(service.getRepository(), times(1)).deleteBySchemaAndId(anyString(), anyString());
    }

    @Test
    void delete_instanceNotFound_throwException() {
        try {
            ObjectInstanceServiceImpl service = setupDeleteCenario(Optional.empty());

            service.deleteById(SCHEMA_NAME, INSTANCE_ID);

            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("", SCHEMA_NAME + " with " + INSTANCE_ID + " not found", e.getMessage());
        }

    }

    private ObjectInstanceServiceImpl setupDeleteCenario(Optional instanceReturn) {
        ObjectInstanceRepository repository = mock( ObjectInstanceRepository.class );
        ObjectInstanceServiceImpl service = new ObjectInstanceServiceImpl(repository, null);

        when(repository.findBySchemaAndId(eq(SCHEMA_NAME), eq(INSTANCE_ID))).thenReturn(
                instanceReturn
        );
        return service;
    }

    private List createInstanceList() {
        List<ObjectInstance> instances = new ArrayList<>();
        instances.add(createObjectInstance());
        return instances;
    }

    private ObjectInstance createObjectInstance() {
        return ObjectInstanceBuilder.create()
                .id(INSTANCE_ID)
                .schema(SCHEMA_NAME)
                .attributes(createAttributes())
                .build();
    }

    private Map<String, Object> createAttributes() {
        return new HashMap<>(){{
            put("name", "XPTO OBJECT");
            put("value", 12.5);
            put("attributes", new ArrayList<>());
            put("quantity", 10);
            put("description", "blablabla");
            put("XZ", new HashMap<>());
        }};
    }

    private Map<String, Object> createBrokenAttributes() {
        var attributes = createAttributes();
        attributes.put("XZ", 1);
        return attributes;
    }

    private Schema createSchema() {
        Schema schema = new Schema();
        schema.setId(SCHEMA_ID);
        schema.setName(SCHEMA_NAME);
        schema.setAttributes(createSchemaAttributes());
        return schema;
    }

    private Map<String, String> createSchemaAttributes() {
        return new HashMap() {{
            put("name", "STRING");
            put("value", "DOUBLE");
            put("attributes", "LIST-STRING");
            put("quantity", "INTEGER");
            put("description", "TEXT");
            put("XZ", "OBJECT");
        }};
    }


}
