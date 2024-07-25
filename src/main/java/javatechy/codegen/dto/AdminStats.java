package javatechy.codegen.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AdminStats {

    @Id
    private Long id;

    private int userCount;
    private int projectCount;

    @Transient
    private List<ProjectCountPerDay> projectsPerDay;




    public AdminStats(int userCount, int projectCount, List<ProjectCountPerDay> projectsPerDay) {
        this.userCount = userCount;
        this.projectCount = projectCount;
        this.projectsPerDay = projectsPerDay;
    }


    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    public List<ProjectCountPerDay> getProjectsPerDay() {
        return projectsPerDay;
    }

    public void setProjectsPerDay(List<ProjectCountPerDay> projectsPerDay) {
        this.projectsPerDay = projectsPerDay;
    }
}
