package br.com.ronistone.xyinc.controller;

import br.com.ronistone.xyinc.model.Attributes;
import br.com.ronistone.xyinc.model.ObjectInstance;
import br.com.ronistone.xyinc.model.builder.ObjectInstanceBuilder;
import br.com.ronistone.xyinc.service.ObjectInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
public class ObjectInstanceController {

    private final ObjectInstanceService objectInstanceService;

    @Autowired
    public ObjectInstanceController(ObjectInstanceService objectInstanceService) {
        this.objectInstanceService = objectInstanceService;
    }

    @PostMapping("/{resource}")
    public ResponseEntity createInstance(@RequestBody Attributes instanceAttributes,
                                         @PathVariable String resource) {

        ObjectInstance objectInstance = attributesToObjectInstance(instanceAttributes);

        Optional<ObjectInstance> response =  objectInstanceService.createObjectInstance(resource, objectInstance);
        return createResponse(response);
    }

    @GetMapping("/{resource}")
    public ResponseEntity list(@PathVariable String resource) {

        Optional<List<ObjectInstance>> response = objectInstanceService.findAllInstances(resource);

        if(response.isPresent()) {
            return createResponse(objectInstancesToAttributesDTO(response));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{resource}/{id}")
    public ResponseEntity findById(@PathVariable String resource, @PathVariable String id) {

        Optional<ObjectInstance> response = objectInstanceService.findById(resource, id);

        return createResponse(response);
    }

    @DeleteMapping("/{resource}/{id}")
    public ResponseEntity deleteById(@PathVariable String resource, @PathVariable String id) {

        objectInstanceService.deleteById(resource, id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{resource}/{id}")
    public ResponseEntity updateById(@PathVariable String resource,
                                     @PathVariable String id,
                                     @RequestBody Attributes instanceAttributes) {
        ObjectInstance objectInstance = attributesToObjectInstance(instanceAttributes);

        Optional<ObjectInstance> response = objectInstanceService.updateById(resource, id, objectInstance);

        return createResponse(response);
    }

    private ObjectInstance attributesToObjectInstance(Attributes instanceAttributes) {
        return ObjectInstanceBuilder.create()
                .withAttributes(instanceAttributes)
                .build();
    }

    private List<Attributes> objectInstancesToAttributesDTO(Optional<List<ObjectInstance>> response) {
        return response.get().stream()
                .map(instance -> {
                        Attributes attributes = instance.getAttributes();
                        attributes.put("id", instance.getId());
                        return attributes;
                    }
                )
                .collect(Collectors.toList());
    }

    private ResponseEntity createResponse(Optional<ObjectInstance> response) {
        if (response.isPresent()) {
            Attributes attributes = response.get().getAttributes();
            attributes.put("id", response.get().getId());
            return ResponseEntity.ok(attributes);
        }

        return ResponseEntity.notFound().build();
    }

    private ResponseEntity createResponse(List response) {
        if (!CollectionUtils.isEmpty(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

}
