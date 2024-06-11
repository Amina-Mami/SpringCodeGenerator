package javatechy.codegen.service.impl;

import javatechy.codegen.Repository.JsonFileRepository;
import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.User;
import javatechy.codegen.service.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonFileServiceImpl implements JsonFileService {
    @Autowired
    private JsonFileRepository jsonFileRepository;

    @Override
    public JsonFile saveFileMetadata(String filePath, User user,String projectName) {
        JsonFile jsonFile = new JsonFile(filePath, user);
        jsonFile.setProjectName(projectName);
        return jsonFileRepository.save(jsonFile);
    }
    @Override
    public JsonFile findById(Long id) {
        return jsonFileRepository.findOne(id);
    }

    @Override
    public List<JsonFile> getAllProjects() {
        return jsonFileRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jsonFileRepository.deleteById(id);
    }

}






