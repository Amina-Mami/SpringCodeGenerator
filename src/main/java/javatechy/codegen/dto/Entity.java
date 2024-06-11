package javatechy.codegen.dto;

import java.util.List;

public class Entity {

    private String name;
    private Boolean crud;
    private List<Field> fields;
    //works
    private List<Relationship> relationships;
    private List<EnumDefinition> enums;
    public List<EnumDefinition> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumDefinition> enums) {
        this.enums = enums;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCrud() {
        return crud;
    }

    public void setCrud(Boolean crud) {
        this.crud = crud;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
}
