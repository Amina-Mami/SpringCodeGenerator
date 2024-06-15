package javatechy.codegen.service;

import javatechy.codegen.dto.Request;

import java.io.IOException;

public interface SwaggerService {


    void createSwaggerConfig(Request request) throws IOException;
}
