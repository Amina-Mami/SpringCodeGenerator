package javatechy.codegen.Repository;

import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JsonFileRepository extends JpaRepository<JsonFile, Long> {
    void deleteById(Long id);
    List<JsonFile> findByUserId(Long userId);

    JsonFile findByUserAndProjectName(User user, String projectName);
}
