package br.com.ronistone.xyinc.controller;

import br.com.ronistone.xyinc.exception.CantDeleteException;
import br.com.ronistone.xyinc.service.ObjectInstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class ObjectInstanceController {

    private final ObjectInstanceService objectInstanceService;

    public ObjectInstanceController(ObjectInstanceService objectInstanceService) {
        this.objectInstanceService = objectInstanceService;
    }

    @PostMapping("/{name}")
    public ResponseEntity createInstance(@RequestBody Map<String,Object> instanceAttributes,
                                         @PathVariable String name) {
        Optional<Map<String,Object>> response =  objectInstanceService.create(name, instanceAttributes);
        return createResponse(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity list(@PathVariable String name) {

        Optional<List<Map<String,Object>>> response = objectInstanceService.findAll(name);

        return createResponse(response);
    }

    @GetMapping("/{name}/{id}")
    public ResponseEntity findById(@PathVariable String name, @PathVariable String id) {

        Optional<Map<String,Object>> response = objectInstanceService.findById(name, id);

        return createResponse(response);
    }

    @DeleteMapping("/{name}/{id}")
    public ResponseEntity deleteById(@PathVariable String name, @PathVariable String id) {
        try {
            objectInstanceService.deleteById(name, id);
            return ResponseEntity.ok().build();
        } catch (CantDeleteException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{name}/{id}")
    public ResponseEntity updateById(@PathVariable String name,
                                     @PathVariable String id,
                                     @RequestBody Map<String,Object> instanceAttributes) {
        Optional<Map<String,Object>> response = objectInstanceService.updateById(name, id, instanceAttributes);

        return createResponse(response);
    }

    private ResponseEntity createResponse(Optional response) {
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        }

        return ResponseEntity.notFound().build();
    }

}
