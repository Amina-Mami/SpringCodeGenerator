package javatechy.codegen.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class JsonFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath;

    private String date;

    private String projectName;

    @Lob
    private String requestDataJson;

    @ManyToOne
    private User user;

    public JsonFile(String filePath, User user) {
        this.filePath = filePath;
        this.user = user;
        this.date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()); // Automatically set the date to the current date in desired format
    }

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            this.date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        }
    }

    public static JsonFile fromRequestData(String projectName, String requestDataJson, User user) {
        JsonFile jsonFile = new JsonFile();
        jsonFile.setProjectName(projectName);
        jsonFile.setRequestDataJson(requestDataJson);
        jsonFile.setUser(user);
        jsonFile.setDate(String.valueOf(new Date()));
        return jsonFile;
    }

    public void setEntities(List<javatechy.codegen.dto.Entity> entities) {
    }

    public void setEnums(List<EnumDefinition> enums) {
    }
}
