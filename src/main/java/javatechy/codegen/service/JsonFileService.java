package javatechy.codegen.service;

import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.Properties;
import javatechy.codegen.dto.Request;
import javatechy.codegen.dto.User;

import java.util.List;

public interface JsonFileService {


   // JsonFile saveFileMetadata(String filePath, User user, String projectName);

    //    @Override
    //    public JsonFile saveFileMetadata(String filePath, User user,String projectName) {
    //        JsonFile jsonFile = new JsonFile(filePath, user);
    //        jsonFile.setProjectName(projectName);
    //
    //        return jsonFileRepository.save(jsonFile);
    //    }
    JsonFile saveFileMetadata(String filePath, User user, String projectName, Request requestData);

    JsonFile findById(Long id);


    void updateProject(JsonFile project);

    List<JsonFile> getAllProjects();

    List<JsonFile> getProjectsByUserId(Long userId);

    void deleteById(Long id);


    void saveOrUpdate(JsonFile existingProject);


}
