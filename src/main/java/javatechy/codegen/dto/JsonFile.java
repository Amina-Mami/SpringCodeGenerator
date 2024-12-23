package javatechy.codegen.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public void setEntities(List<javatechy.codegen.dto.Entity> entities) {
    }

    public void setEnums(List<EnumDefinition> enums) {
    }
    // Convenience method to set requestDataJson as a Map
    public void setRequestDataJson(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.requestDataJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }

    public Map<String, Object> getRequestDataJson() {
        ObjectMapper mapper = new ObjectMapper();
        if (this.requestDataJson == null) {
            return new HashMap<>(); // Return an empty map if the JSON is null
        }
        try {
            return mapper.readValue(this.requestDataJson, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
            return new HashMap<>(); // Return an empty map on failure
        }
    }



    // Convenience method to get requestDataJson as a Map

}
