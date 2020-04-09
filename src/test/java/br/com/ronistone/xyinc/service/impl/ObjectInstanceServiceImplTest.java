package br.com.ronistone.xyinc.service.impl;

import br.com.ronistone.xyinc.model.Attributes;
import br.com.ronistone.xyinc.model.ObjectInstance;
import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.model.builder.ObjectInstanceBuilder;
import br.com.ronistone.xyinc.repository.ObjectInstanceRepository;
import br.com.ronistone.xyinc.service.SchemaService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        Optional result = service.createObjectInstance(SCHEMA_NAME, createObjectInstance());

        verify(service.getObjectInstanceRepository(), times(1)).save(any(ObjectInstance.class));
        assertTrue("the instance must been created", result.isPresent());
    }

    @Test
    void create_attributesWithTypeWrong_fail() {
        try {
            ObjectInstanceServiceImpl service = setupCreateCenario(Optional.of(createSchema()));

            Optional result = service.createObjectInstance(SCHEMA_NAME, createObjectInstance(createBrokenAttributes()));

            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("The message must be", "XZ must been OBJECT", e.getMessage());
        }
    }

    @Test
    void create_schemaNotFound_fail() {
        ObjectInstanceServiceImpl service = setupCreateCenario(Optional.empty());

        Optional result = service.createObjectInstance(SCHEMA_NAME, createObjectInstance());

        verify(service.getObjectInstanceRepository(), times(0)).save(any(ObjectInstance.class));
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

        Optional result = service.updateById(SCHEMA_NAME, INSTANCE_ID, createObjectInstance());

        verify(service.getObjectInstanceRepository(), times(1)).save(any(ObjectInstance.class));
        assertTrue("the instance must been created", result.isPresent());
    }

    @Test
    void update_schemaNotFound_fail() {
        ObjectInstanceServiceImpl service = setupUpdateCenario(Optional.empty(), Optional.empty());

        Optional result = service.updateById(SCHEMA_NAME, INSTANCE_ID, createObjectInstance());

        verify(service.getObjectInstanceRepository(), times(0)).save(any(ObjectInstance.class));
        assertFalse("the instance must not be created", result.isPresent());
    }

    private ObjectInstanceServiceImpl setupUpdateCenario(Optional instanceReturn, Optional withSchema) {
        ObjectInstanceRepository repository = mock( ObjectInstanceRepository.class );
        SchemaService schemaService = mock( SchemaService.class );
        ObjectInstanceServiceImpl service = new ObjectInstanceServiceImpl(repository, schemaService);

        when(repository.findBySchemaAndId(eq(SCHEMA_NAME), eq(SCHEMA_ID))).thenReturn(
                instanceReturn
        );
        when(schemaService.findByName(eq(SCHEMA_NAME))).thenReturn(withSchema);
        when(repository.save(any(ObjectInstance.class))).thenReturn(createObjectInstance());
        return service;
    }

    @Test
    void findAll_save_success() {

        ObjectInstanceServiceImpl service = setupFindAllCenario(Optional.of(createInstanceList()));

        Optional result = service.findAllInstances(SCHEMA_NAME);

        assertTrue("the instance must been found", result.isPresent());
    }

    @Test
    void findAll_instancesNotFound_fail() {
        ObjectInstanceServiceImpl service = setupFindAllCenario(Optional.empty());

        Optional result = service.findAllInstances(SCHEMA_NAME);

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

        verify(service.getObjectInstanceRepository(), times(1)).deleteBySchemaAndId(anyString(), anyString());
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

    private ObjectInstance createObjectInstance(){
        return createObjectInstance(createAttributes());
    }

    private ObjectInstance createObjectInstance(Attributes attributes) {
        return ObjectInstanceBuilder.create()
                .withId(INSTANCE_ID)
                .withSchema(SCHEMA_NAME)
                .withAttributes(attributes)
                .build();
    }

    private Attributes createAttributes() {
        return new Attributes(){{
            put("name", "XPTO OBJECT");
            put("value", 12.5);
            put("withAttributes", new ArrayList<>());
            put("quantity", 10);
            put("description", "blablabla");
            put("XZ", new HashMap<>());
        }};
    }

    private Attributes createBrokenAttributes() {
        Attributes withAttributes = createAttributes();
        withAttributes.put("XZ", 1);
        return withAttributes;
    }

    private Schema createSchema() {
        Schema withSchema = new Schema();
        withSchema.setId(SCHEMA_ID);
        withSchema.setName(SCHEMA_NAME);
        withSchema.setAttributes(createSchemaAttributes());
        return withSchema;
    }

    private Attributes createSchemaAttributes() {
        return new Attributes() {{
            put("name", "STRING");
            put("value", "DOUBLE");
            put("withAttributes", "LIST-STRING");
            put("quantity", "INTEGER");
            put("description", "TEXT");
            put("XZ", "OBJECT");
        }};
    }


}
