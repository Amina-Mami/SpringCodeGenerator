package javatechy.codegen.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
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

            String projectName = request.getProjectName();


            String jsonConfig = new ObjectMapper().writeValueAsString(request);
            String filename = "request-" + UUID.randomUUID().toString() + ".json";
            Path jsonFilePath = Paths.get("C:\\Users\\User\\Desktop\\json\\" + filename);
            Files.write(jsonFilePath, jsonConfig.getBytes(StandardCharsets.UTF_8));


            JsonFile savedFile = jsonFileService.saveFileMetadata(jsonFilePath.toString(), user, projectName);


            Path projectDirectory = Paths.get("C:\\Users\\User\\Desktop\\projects\\" + projectName);
            if (!Files.exists(projectDirectory)) {
                Files.createDirectories(projectDirectory);
            }


            Path springBootDirectory = projectDirectory.resolve("spring-boot");
            if (!Files.exists(springBootDirectory)) {
                Files.createDirectories(springBootDirectory);
            }


            projectService.createProject(request, springBootDirectory);


            if (request.isEnableFrontendReact()) {
                generateReactProject(projectDirectory, "react-frontend");
            }

            return new ResponseEntity<>(new Response("Project created successfully", "200", String.valueOf(savedFile.getId())), HttpStatus.OK);
        } catch (IOException | InterruptedException e) {
            logger.error("Error creating project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
        }
    }



    private void generateReactProject(Path projectDirectory, String projectName) throws IOException, InterruptedException {
        Path reactProjectPath = projectDirectory.resolve(projectName);
        File directory = reactProjectPath.toFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        logger.info("React project directory: " + reactProjectPath);

        String command = "npm create vite@latest -- --template react " + projectName;
        ProcessBuilder builder = new ProcessBuilder()
                .command("cmd.exe", "/c", command)
                .directory(projectDirectory.toFile())
                .inheritIO();

        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            logger.info("React project generated successfully at: " + reactProjectPath);


        } else {
            logger.error("Error generating React project. Exit code: " + exitCode);
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


            Path projectDirectory = Paths.get("C:\\Users\\User\\Desktop\\projects\\" + request.getProjectName());
            if (!Files.exists(projectDirectory)) {
                Files.createDirectories(projectDirectory);
            }


            Path springBootDirectory = projectDirectory.resolve("spring-boot");
            if (!Files.exists(springBootDirectory)) {
                Files.createDirectories(springBootDirectory);
            }

            projectService.createProject(request, springBootDirectory);
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
    public ResponseEntity<Resource> downloadProject(@PathVariable Long projectId) {
        try {
            JsonFile jsonFile = jsonFileService.findById(projectId);
            if (jsonFile == null) {
                logger.warn("JsonFile not found for projectId: " + projectId);
                return ResponseEntity.notFound().build();
            }

            Path projectPath = Paths.get(jsonFile.getFilePath());
            if (!Files.exists(projectPath)) {
                logger.warn("Project path does not exist: " + projectPath);
                return ResponseEntity.notFound().build();
            }

            ByteArrayResource resource = generateProjectZip(projectPath);
            if (resource == null || resource.contentLength() == 0) {
                logger.error("Generated project ZIP is empty or null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            logger.error("Error downloading project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private ByteArrayResource generateProjectZip(Path projectPath) throws IOException {
        Path zipFilePath = Files.createTempFile("project", ".zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
            Files.walk(projectPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(projectPath.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            logger.error("Error adding file to ZIP: " + path, e);
                        }
                    });
        }
        byte[] zipBytes = Files.readAllBytes(zipFilePath);
        return new ByteArrayResource(zipBytes);
    }








}
