package javatechy.codegen.service;
import javatechy.codegen.dto.Request;
import java.io.IOException;

public interface EntityFileCreatorService {

    void createEntityFiles(Request request) throws IOException;

}