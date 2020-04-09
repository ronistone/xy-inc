package br.com.ronistone.xyinc.model;

import java.util.List;
import java.util.Map;

public enum AttributesTypes {

    LIST(List.class),
    STRING(String.class),
    DOUBLE(Double.class),
    INTEGER(Integer.class),
    TEXT(String.class),
    OBJECT(Map.class),
    ;


    private Class type;

    AttributesTypes(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }
}
