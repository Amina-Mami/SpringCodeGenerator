
package javatechy.codegen.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Database {
    private Boolean databaseEnabled;

    private String databaseType;

    private Integer port;
    private String databaseName;
    private String userName;
    private String password;


    public Boolean getDatabaseEnabled() {
        return databaseEnabled;
    }

    public void setDatabaseEnabled(Boolean databaseEnabled) {
        this.databaseEnabled = databaseEnabled;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }



    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
