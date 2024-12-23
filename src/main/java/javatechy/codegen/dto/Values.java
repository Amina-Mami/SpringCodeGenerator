package javatechy.codegen.dto;

public class Values {
    private String values;

    // Constructeur par défaut
    public Values() {
    }

    // Constructeur avec paramètres
    public Values(String values) {
        this.values = values;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
