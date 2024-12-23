package javatechy.codegen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment {

    private Boolean isDockerEnabled;

    public Boolean getIsDockerEnabled() {
        return isDockerEnabled;
    }

    public void setIsDockerEnabled(Boolean isDockerEnabled) {
        this.isDockerEnabled = isDockerEnabled;
    }

}
