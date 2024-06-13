
package javatechy.codegen.dto;

import javax.persistence.*;


public class Properties {

    private String name;
    private String groupId;
    private String artifactId;
    private String description;
    private String applicationClassName;
    private String language;
    private String applicationPort;
    private String developerName;
    private String springVerison;
    private Boolean isConstantFile;
    private Boolean isGlobalExceptionEnabled;
    private Boolean isLombokEnabled;
    private Boolean isActuatorEnabled;
    private String basePackage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;








    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBasePackage() {
        return this.basePackage;
    }
    public String getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(String applicationPort) {
        this.applicationPort = applicationPort;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getApplicationClassName() {
        return applicationClassName;
    }

    public void setApplicationClassName(String applicationClassName) {
        this.applicationClassName = applicationClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Boolean getIsActuatorEnabled() {
        return isActuatorEnabled;
    }

    public void setIsActuatorEnabled(Boolean isActuatorEnabled) {
        this.isActuatorEnabled = isActuatorEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSpringVerison() {
        return springVerison;
    }

    public void setSpringVerison(String springVerison) {
        this.springVerison = springVerison;
    }

    public Boolean getIsConstantFile() {
        return isConstantFile;
    }

    public void setIsConstantFile(Boolean isConstantFile) {
        this.isConstantFile = isConstantFile;
    }

    public Boolean getIsGlobalExceptionEnabled() {
        return isGlobalExceptionEnabled;
    }

    public void setIsGlobalExceptionEnabled(Boolean isGlobalExceptionEnabled) {
        this.isGlobalExceptionEnabled = isGlobalExceptionEnabled;
    }

    public Boolean getIsLombokEnabled() {
        return isLombokEnabled;
    }

    public void setIsLombokEnabled(Boolean isLombokEnabled) {
        this.isLombokEnabled = isLombokEnabled;
    }


}
