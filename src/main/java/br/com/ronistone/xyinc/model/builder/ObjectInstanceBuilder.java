package br.com.ronistone.xyinc.model.builder;

import br.com.ronistone.xyinc.model.Attributes;
import br.com.ronistone.xyinc.model.ObjectInstance;


public class ObjectInstanceBuilder {

    private ObjectInstance objectInstance;

    private ObjectInstanceBuilder(){}
    private ObjectInstanceBuilder(ObjectInstance objectInstance){
        this.objectInstance = objectInstance;
    }

    public static ObjectInstanceBuilder create(){
        return new ObjectInstanceBuilder(new ObjectInstance());
    }

    public ObjectInstanceBuilder withId(String id) {
        objectInstance.setId(id);
        return this;
    }

    public ObjectInstanceBuilder withSchema(String schema) {
        objectInstance.setSchema(schema);
        return this;
    }

    public ObjectInstanceBuilder withAttributes(Attributes attributes) {
        objectInstance.setAttributes(attributes);
        return this;
    }

    public ObjectInstance build(){
        return objectInstance;
    }


}
