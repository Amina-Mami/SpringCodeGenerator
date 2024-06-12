package javatechy.codegen.dto;

import java.util.List;

public class EnumDefinition {

    private String name;

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }

    private List<Values> values;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
