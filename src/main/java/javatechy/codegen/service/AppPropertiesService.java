package javatechy.codegen.service;

import java.io.IOException;

import javatechy.codegen.dto.Request;

public interface AppPropertiesService {
    void generateApplicationProperties(Request request) throws IOException;
}
