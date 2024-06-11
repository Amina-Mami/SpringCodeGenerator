
package javatechy.codegen.dto;




public class Database {

    private Boolean isDatabaseEnabled;
    private String databaseType;
    private String userName;
    private String password;


    public Boolean getIsDatabaseEnabled() {
        return isDatabaseEnabled;
    }

    public void setIsDatabaseEnabled(Boolean isDatabaseEnabled) {
        this.isDatabaseEnabled = isDatabaseEnabled;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
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
