package javatechy.codegen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


public class Relationship {

    private String type;
    private String sourceEntity;
    private String targetEntity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }




}
