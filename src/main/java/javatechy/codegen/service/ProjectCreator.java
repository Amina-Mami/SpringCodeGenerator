package javatechy.codegen.service;

import java.io.IOException;

import javatechy.codegen.dto.Request;

public interface ProjectCreator {
    void generateEmptyProject(Request request) throws IOException;

    void initiliaze(Request request);

    void generateMainClass(Request request) throws IOException;
}
