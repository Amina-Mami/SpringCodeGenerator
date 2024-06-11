package javatechy.codegen.service;

import javatechy.codegen.dto.Request;

public interface DatabaseService {
    void addDatabaseProps(Request request);

    void addDatabaseDto(Request request);
}
