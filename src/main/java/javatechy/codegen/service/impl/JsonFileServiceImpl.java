package javatechy.codegen.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javatechy.codegen.Repository.JsonFileRepository;
import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.Properties;
import javatechy.codegen.dto.Request;
import javatechy.codegen.dto.User;
import javatechy.codegen.service.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonFileServiceImpl implements JsonFileService {
    @Autowired
    private JsonFileRepository jsonFileRepository;

//@Override
//public JsonFile saveFileMetadata(String filePath, User user, String projectName, Request requestData) {
//    JsonFile jsonFile = new JsonFile();
//    jsonFile.setFilePath(filePath);
//    jsonFile.setUser(user);
//    jsonFile.setProjectName(projectName);
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    try {
//        String requestDataJson = objectMapper.writeValueAsString(requestData);
//        jsonFile.setRequestDataJson(requestDataJson);
//    } catch (JsonProcessingException e) {
//
//    }
//
//    return jsonFileRepository.save(jsonFile);
//}
    @Override
    public JsonFile findById(Long id) {
        return jsonFileRepository.findOne(id);
    }

@Override
    public void updateProject(JsonFile project) {
        jsonFileRepository.save(project);
    }


    @Override
    public List<JsonFile> getAllProjects() {
        return jsonFileRepository.findAll();
    }
    @Override
    public List<JsonFile> getProjectsByUserId(Long userId) {
        return jsonFileRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        jsonFileRepository.deleteById(id);
    }

    @Override
    public void saveOrUpdate(JsonFile existingProject) {
        if (existingProject != null) {
            jsonFileRepository.save(existingProject);
            System.out.println("Project saved with ID: " + existingProject.getId());
        } else {
            throw new IllegalArgumentException("The project to be updated cannot be null.");
        }
    }



}






