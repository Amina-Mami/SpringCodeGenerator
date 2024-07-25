package javatechy.codegen.Repository;

import javatechy.codegen.dto.AdminStats;
import javatechy.codegen.dto.ProjectCountPerDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminStatsRepository extends JpaRepository<AdminStats, Long> {
    @Query("SELECT COUNT(u) FROM User u")
    int getUserCount();

    @Query("SELECT COUNT(j) FROM JsonFile j")
    int getProjectCount();

    @Query("SELECT new javatechy.codegen.dto.ProjectCountPerDay(j.date, COUNT(j)) " +
            "FROM JsonFile j " +
            "GROUP BY j.date")
    List<ProjectCountPerDay> getProjectsPerDay();

}
