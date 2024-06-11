package javatechy.codegen.service;

import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.User;

import java.util.List;

public interface JsonFileService {


    JsonFile saveFileMetadata(String filePath, User user, String projectName);

    JsonFile findById(Long id);


    List<JsonFile> getAllProjects();

    void deleteById(Long id);


}
