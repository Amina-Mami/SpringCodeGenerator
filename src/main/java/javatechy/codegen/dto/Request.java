package javatechy.codegen.dto;

import java.util.Date;
import java.util.List;

public class Request {

    private Date date;
    private String projectName;
    private List<Entity> entities;
    private List<EnumDefinition> enums;
    private Properties properties;
    private Swagger swagger;
    private Database database;
    private Logging logging;
    private boolean enableFrontendReact;

    // Getters and Setters

    public boolean isEnableFrontendReact() {
        return enableFrontendReact;
    }

    public void setEnableFrontendReact(boolean enableFrontendReact) {
        this.enableFrontendReact = enableFrontendReact;
    }
    public Date getTimestamp() {
        return date;
    }

    public void setTimestamp(Date timestamp) {
        this.date = timestamp;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<EnumDefinition> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumDefinition> enums) {
        this.enums = enums;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }



}
