
package javatechy.codegen.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Swagger {
    private Boolean isEnabled;
    private String apiDocName;
    private String developerEmailId;



    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getApiDocName() {
        return apiDocName;
    }

    public void setApiDocName(String apiDocName) {
        this.apiDocName = apiDocName;
    }

    public String getDeveloperEmailId() {
        return developerEmailId;
    }

    public void setDeveloperEmailId(String developerEmailId) {
        this.developerEmailId = developerEmailId;
    }

}
