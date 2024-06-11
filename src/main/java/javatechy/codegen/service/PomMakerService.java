package javatechy.codegen.service;

import java.io.IOException;

import javatechy.codegen.dto.Request;

public interface PomMakerService {

    void createPomXml(Request request) throws IOException;

    void addDependencies(Request request);
}
