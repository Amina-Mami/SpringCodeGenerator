package javatechy.codegen.service;

import java.io.IOException;

import javatechy.codegen.dto.Request;

public interface ControllerGenService {
    void generateControllers(Request request) throws IOException;
}
