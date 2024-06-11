package javatechy.codegen.dto;

import java.util.Date;
import java.util.List;

public class Request {

    private Date timestamp;
    private String projectName;
    private List<Entity> entities;

    private List<EnumDefinition> enums;

    private Properties properties;
    private Swagger swagger;
    private Database database;
    private Logging logging;


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }



    public List<EnumDefinition> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumDefinition> enums) {
        this.enums = enums;
    }

}
