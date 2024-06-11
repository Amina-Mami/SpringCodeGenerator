
package javatechy.codegen.dto;
import javax.persistence.*;


public class Logging {

    private Boolean isEnabledLog;
    private String loggingFile;
    private String rotationSize;

    public Boolean getIsEnabledLog() {
        return isEnabledLog;
    }

    public void setIsEnabledLog(Boolean isEnabledLog) {
        this.isEnabledLog = isEnabledLog;
    }

    public String getLoggingFile() {
        return loggingFile;
    }

    public void setLoggingFile(String loggingFile) {
        this.loggingFile = loggingFile;
    }

    public String getRotationSize() {
        return rotationSize;
    }

    public void setRotationSize(String rotationSize) {
        this.rotationSize = rotationSize;
    }

}
