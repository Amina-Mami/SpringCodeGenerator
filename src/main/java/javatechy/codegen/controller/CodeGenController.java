package javatechy.codegen.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javatechy.codegen.Repository.JsonFileRepository;
import javatechy.codegen.dto.JsonFile;
import javatechy.codegen.dto.User;
import javatechy.codegen.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
    @Autowired
    JsonFileRepository jsonFileRepository;
    private static final Logger logger = Logger.getLogger(CodeGenController.class);
    @PostMapping(value = "/create/{userId}", produces = "application/zip")
    public ResponseEntity<?> createProject(@PathVariable Long userId, @RequestBody Request request) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().body(new Response("User ID is required", "400", "User ID is required"));
            }

            User user = userService.findUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(new Response("User not found", "404", "User not found"), HttpStatus.NOT_FOUND);
            }

            String projectName = request.getProjectName();
            Path projectDirectory = Files.createTempDirectory("projects-" + projectName);
            Path springBootDirectory = projectDirectory.resolve("spring-boot");
            Files.createDirectories(springBootDirectory);

            projectService.createProject(request, springBootDirectory);

            if (request.isEnableFrontendReact()) {
                generateReactProject(projectDirectory, "react-frontend");
            }

            boolean enableFrontendReact = request.isEnableFrontendReact();
            Path zipFilePath = zipProjectDirectoryContents(projectDirectory, projectName, enableFrontendReact);

            JsonFile existingProject = jsonFileRepository.findByUserAndProjectName(user, projectName);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestDataMap = objectMapper.convertValue(request, Map.class);

            if (existingProject != null) {
                existingProject.setFilePath(zipFilePath.toString());
                existingProject.setRequestDataJson(requestDataMap); // Use the convenience method
                jsonFileRepository.save(existingProject);
            } else {
                JsonFile newProject = new JsonFile();
                newProject.setProjectName(projectName);
                newProject.setFilePath(zipFilePath.toString());
                newProject.setUser(user);
                newProject.setRequestDataJson(requestDataMap); // Use the convenience method
                jsonFileRepository.save(newProject);
            }

            return prepareDownloadResponse(zipFilePath, projectName);

        } catch (IOException | InterruptedException e) {
            logger.error("Error creating project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
        }
    }


//    @PostMapping(value = "/create/{userId}", produces = "application/zip")
//    public ResponseEntity<?> createProject(@PathVariable Long userId, @RequestBody Request request) {
//        try {
//            if (userId == null) {
//                return ResponseEntity.badRequest().body(new Response("User ID is required", "400", "User ID is required"));
//            }
//
//            User user = userService.findUserById(userId);
//            if (user == null) {
//                return new ResponseEntity<>(new Response("User not found", "404", "User not found"), HttpStatus.NOT_FOUND);
//            }
//
//            String projectName = request.getProjectName();
//            Path projectDirectory = Files.createTempDirectory("projects-" + projectName);
//            Path springBootDirectory = projectDirectory.resolve("spring-boot");
//            Files.createDirectories(springBootDirectory);
//
//            projectService.createProject(request, springBootDirectory);
//
//            if (request.isEnableFrontendReact()) {
//                generateReactProject(projectDirectory, "react-frontend");
//            }
//
//            boolean enableFrontendReact = request.isEnableFrontendReact();
//            Path zipFilePath = zipProjectDirectoryContents(projectDirectory, projectName, enableFrontendReact);
//
//
//            JsonFile existingProject = jsonFileRepository.findByUserAndProjectName(user, projectName);
//
//            if (existingProject != null) {
//
//                existingProject.setFilePath(zipFilePath.toString());
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//                    String requestDataJson = objectMapper.writeValueAsString(request);
//                    existingProject.setRequestDataJson(requestDataJson);
//                } catch (JsonProcessingException e) {
//                    logger.error("Error serializing Request object to JSON", e);
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
//                }
//                jsonFileRepository.save(existingProject);
//            } else {
//
//                JsonFile newProject = new JsonFile();
//                newProject.setProjectName(projectName);
//                newProject.setFilePath(zipFilePath.toString());
//                newProject.setUser(user);
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//                    String requestDataJson = objectMapper.writeValueAsString(request);
//                    newProject.setRequestDataJson(requestDataJson);
//                } catch (JsonProcessingException e) {
//                    logger.error("Error serializing Request object to JSON", e);
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
//                }
//                jsonFileRepository.save(newProject);
//            }
//
//            return prepareDownloadResponse(zipFilePath, projectName);
//
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error creating project", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error creating project", "500", e.getMessage()));
//        }
//    }




    private Path zipProjectDirectoryContents(Path projectDirectory, String projectName, boolean enableFrontendReact) throws IOException {
    Path zipFilePath = Files.createTempFile(projectName, ".zip");
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {

        Files.walk(projectDirectory)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    try {
                        Path relativePath = projectDirectory.relativize(path);
                        ZipEntry zipEntry = new ZipEntry(projectName + "/" + relativePath.toString());
                        zipOutputStream.putNextEntry(zipEntry);
                        Files.copy(path, zipOutputStream);
                        zipOutputStream.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error adding file to zip", e);
                    }
                });
    }
    return zipFilePath;
}


//    private void addDirectoryToZip(Path sourceDir, Path projectDirectory, ZipOutputStream zipOutputStream) throws IOException {
//        Files.walk(sourceDir)
//                .filter(path -> !Files.isDirectory(path))
//                .forEach(path -> {
//                    try {
//                        Path relativePath = projectDirectory.relativize(path);
//                        ZipEntry zipEntry = new ZipEntry(relativePath.toString());
//                        zipOutputStream.putNextEntry(zipEntry);
//                        Files.copy(path, zipOutputStream);
//                        zipOutputStream.closeEntry();
//                    } catch (IOException e) {
//                        throw new RuntimeException("Error adding directory to zip", e);
//                    }
//                });
//    }



    private ResponseEntity<?> prepareDownloadResponse(Path zipFilePath, String projectName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String contentDisposition = "attachment; filename=\"" + projectName + ".zip\"";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        logger.info("Content-Disposition: " + contentDisposition);
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(zipFilePath));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(zipFilePath))
                .body(resource);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Failed to convert value: %s to required type: %s", ex.getValue(), ex.getRequiredType().getSimpleName());
        return new ResponseEntity<>(new Response("Invalid input", "400", message), HttpStatus.BAD_REQUEST);
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
    @GetMapping("/projects/{userId}")
    public ResponseEntity<List<JsonFile>> getProjectsByUserId(@PathVariable Long userId) {
        try {
            List<JsonFile> projects = jsonFileService.getProjectsByUserId(userId);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/update/{projectId}")
//    public ResponseEntity<JsonFile> getProjectById(@PathVariable Long projectId) {
//        Optional<JsonFile> project = Optional.ofNullable(jsonFileService.findById(projectId));
//        if (project.isPresent()) {
//            return ResponseEntity.ok(project.get());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
@GetMapping("/update/{projectId}")
public ResponseEntity<JsonFile> getProjectById(@PathVariable Long projectId) {
    JsonFile project = jsonFileService.findById(projectId);
    if (project != null) {
        return ResponseEntity.ok(project);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

    @GetMapping("/{projectId}")
public ResponseEntity<Map<String, Object>> getProjectDataById(@PathVariable Long projectId) {
    Optional<JsonFile> project = Optional.ofNullable(jsonFileService.findById(projectId));
    if (project.isPresent()) {
        JsonFile jsonFile = project.get();
        Map<String, Object> response = new HashMap<>();
        response.put("request_data_json", jsonFile.getRequestDataJson());
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody JsonFile updatedProject) {
        try {

            System.out.println("Update request received for project ID: " + projectId);
            System.out.println("Updated project data: " + updatedProject);

            JsonFile existingProject = jsonFileService.findById(projectId);

            if (existingProject == null) {
                System.out.println("Project not found for ID: " + projectId);
                return ResponseEntity.notFound().build();
            }

            if (updatedProject.getProjectName() != null) {
                existingProject.setProjectName(updatedProject.getProjectName());
                System.out.println("Updated project name to: " + updatedProject.getProjectName());
            }

            if (updatedProject.getRequestDataJson() != null) {
                System.out.println("Updating requestDataJson...");
                existingProject.setRequestDataJson(updatedProject.getRequestDataJson());
                System.out.println("Updated requestDataJson to: " + updatedProject.getRequestDataJson());
            }

            jsonFileService.saveOrUpdate(existingProject);
            System.out.println("Project updated successfully!");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/download/{userId}/{projectName}")
//    public ResponseEntity<?> downloadProject(@PathVariable Long userId, @PathVariable String projectName) {
//        try {
//            User user = userService.findUserById(userId);
//            if (user == null) {
//                return new ResponseEntity<>(new Response("User not found", "404", "User not found"), HttpStatus.NOT_FOUND);
//            }
//
//            JsonFile project = jsonFileRepository.findByUserAndProjectName(user, projectName);
//            if (project == null) {
//                return new ResponseEntity<>(new Response("Project not found", "404", "Project not found"), HttpStatus.NOT_FOUND);
//            }
//
//            Path zipFilePath = Paths.get(project.getFilePath());
//            return prepareDownloadResponse(zipFilePath, projectName);
//
//        } catch (IOException e) {
//            logger.error("Error downloading project", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error downloading project", "500", e.getMessage()));
//        }
//    }

    @GetMapping("/download/{userId}/{projectName}")
    public ResponseEntity<?> downloadProject(@PathVariable Long userId, @PathVariable String projectName) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(new Response("User not found", "404", "User not found"), HttpStatus.NOT_FOUND);
            }

            JsonFile project = jsonFileRepository.findByUserAndProjectName(user, projectName);
            if (project == null) {
                return new ResponseEntity<>(new Response("Project not found", "404", "Project not found"), HttpStatus.NOT_FOUND);
            }

            // Convert requestDataJson back to Request object
            ObjectMapper objectMapper = new ObjectMapper();
            Request requestData = objectMapper.convertValue(project.getRequestDataJson(), Request.class);

            // Create project directory from requestDataJson
            String tempDirPrefix = "projects-" + projectName;
            Path projectDirectory = Files.createTempDirectory(tempDirPrefix);
            Path springBootDirectory = projectDirectory.resolve("spring-boot");
            Files.createDirectories(springBootDirectory);

            projectService.createProject(requestData, springBootDirectory);

            if (requestData.isEnableFrontendReact()) {
                generateReactProject(projectDirectory, "react-frontend");
            }

            boolean enableFrontendReact = requestData.isEnableFrontendReact();
            Path zipFilePath = zipProjectDirectoryContents(projectDirectory, projectName, enableFrontendReact);

            return prepareDownloadResponse(zipFilePath, projectName);

        } catch (IOException | InterruptedException e) {
            logger.error("Error downloading project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error downloading project", "500", e.getMessage()));
        }
    }


}
