package br.com.ronistone.xyinc.model.builder;

import br.com.ronistone.xyinc.model.ObjectInstance;

import java.util.Map;

public class ObjectInstanceBuilder {

    private ObjectInstance objectInstance;

    private ObjectInstanceBuilder(){}
    private ObjectInstanceBuilder(ObjectInstance objectInstance){
        this.objectInstance = objectInstance;
    }

    public static ObjectInstanceBuilder create(){
        return new ObjectInstanceBuilder(new ObjectInstance());
    }

    public ObjectInstanceBuilder id(String id) {
        objectInstance.setId(id);
        return this;
    }

    public ObjectInstanceBuilder schema(String schema) {
        objectInstance.setSchema(schema);
        return this;
    }

    public ObjectInstanceBuilder attributes(Map attributes) {
        objectInstance.setAttributes(attributes);
        return this;
    }

    public ObjectInstance build(){
        return objectInstance;
    }


}
