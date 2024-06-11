package javatechy.codegen.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.User;
import javatechy.codegen.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import javatechy.codegen.dto.Request;
import javatechy.codegen.dto.Response;
import javatechy.codegen.service.JsonFileService;
import javatechy.codegen.service.ProjectService;
import org.springframework.core.io.Resource;

import javax.transaction.Transactional;

@Api(value = "Project Controller")
@CrossOrigin(origins = "*")
@RequestMapping("project")
@RestController
public class CodeGenController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private JsonFileService jsonFileService;

    private static final Logger logger = Logger.getLogger(CodeGenController.class);

    @PostMapping(value = "/create/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createProject(@PathVariable Long userId, @RequestBody Request request) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(new Response("User not found", "404", "User not found"), HttpStatus.NOT_FOUND);
            }

            String projectName = request.getProperties().getName();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonConfig = objectMapper.writeValueAsString(request);
            String filename = "request-" + UUID.randomUUID().toString() + ".json";
            Path path = Paths.get("C:\\Users\\User\\Desktop\\json\\" + filename);
            Files.write(path, jsonConfig.getBytes(StandardCharsets.UTF_8));


            JsonFile savedFile = jsonFileService.saveFileMetadata(path.toString(), user, projectName);


            projectService.createProject(request);

            return new ResponseEntity<>(new Response("Project created successfully", "200", String.valueOf(savedFile.getId())), HttpStatus.OK);
        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
        }
    }



    @GetMapping("/json/{jsonFileId}")
    public ResponseEntity<Response> regenerateProject(@PathVariable Long jsonFileId) {
        try {
            JsonFile jsonFile = jsonFileService.findById(jsonFileId);
            if (jsonFile == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(jsonFile.getFilePath());
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }


            String jsonContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);


            ObjectMapper objectMapper = new ObjectMapper();
            Request request;
            try {
                request = objectMapper.readValue(jsonContent, Request.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new Response("Invalid JSON format", "400", e.getMessage()));
            }


            projectService.createProject(request);
            return ResponseEntity.ok(new Response("Project regenerated successfully", "200", ""));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {

            boolean isAuthenticated = userService.authenticateUser(user.getUsername(), user.getPassword());

            if (isAuthenticated) {
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.badRequest().body("Invalid username or password. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }


    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects")
    public ResponseEntity<List<JsonFile>> getAllProjects() {
        try {
            List<JsonFile> projects = jsonFileService.getAllProjects();
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Transactional
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Response> deleteProject(@PathVariable Long projectId) {
        logger.debug("Received request to delete project with ID: " + projectId);

        try {
            jsonFileService.deleteById(projectId);
            logger.debug("Project deleted successfully: " + projectId);
            return ResponseEntity.ok(new Response("Project deleted successfully", "200", ""));
        } catch (Exception e) {
            logger.error("Error while deleting project: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/download/{projectId}")
    public ResponseEntity<Resource> downloadProject(@PathVariable Long projectId) throws IOException {
        ByteArrayResource resource = projectService.generateProjectZip(projectId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}
