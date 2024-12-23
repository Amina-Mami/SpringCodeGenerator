
package javatechy.codegen.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {



    private String name;
    private String type;
    private Boolean isUnique;
    private Boolean isNullable;






    // Standard getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsUnique() {
        return isUnique;
    }

     public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    public Boolean getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
    }




}

