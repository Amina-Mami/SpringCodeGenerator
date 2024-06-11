package javatechy.codegen.service;

import java.io.IOException;
import java.nio.file.Path;

import javatechy.codegen.dto.Request;
import org.springframework.core.io.ByteArrayResource;

public interface ProjectService {

    void createProject(Request request) throws IOException;

    ByteArrayResource generateProjectZip(Long projectId) throws IOException;

    Path zipProjectDirectory(Path projectPath) throws IOException;
}
