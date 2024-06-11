package javatechy.codegen.Repository;

import javatechy.codegen.dto.JsonFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonFileRepository extends JpaRepository<JsonFile, Long> {
    void deleteById(Long id);
}
