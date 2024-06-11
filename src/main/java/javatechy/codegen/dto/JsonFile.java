package javatechy.codegen.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class JsonFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String projectName;

    @ManyToOne
    @JsonIgnore
    private User user;
    public JsonFile(String filePath, User user) {
        this.filePath = filePath;
        this.user = user;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
