package br.com.ronistone.xyinc.controller;

import br.com.ronistone.xyinc.model.Schema;
import br.com.ronistone.xyinc.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schema")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @GetMapping
    public ResponseEntity getAll(){
        return ResponseEntity.ok(schemaService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity getSchemaByName(@PathVariable String name) {

        Optional<Schema> schema = schemaService.findByName(name);
        if(schema.isPresent()) {
            return ResponseEntity.ok(schema.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity createSchema(@RequestBody Schema schema) {

        return ResponseEntity.ok(schemaService.create(schema));

    }

}
