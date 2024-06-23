package javatechy.codegen.service;

import java.io.IOException;
import java.nio.file.Path;

import javatechy.codegen.dto.Request;
import org.springframework.core.io.ByteArrayResource;

public interface ProjectService {





    void createProject(Request request, Path projectDirectory) throws IOException;


}
