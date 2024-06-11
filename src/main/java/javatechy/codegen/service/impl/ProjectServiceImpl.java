package javatechy.codegen.service.impl;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

import javatechy.codegen.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javatechy.codegen.controller.CodeGenController;
import javatechy.codegen.dto.Request;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger logger = Logger.getLogger(CodeGenController.class);
    public static String projectLocation;
    public static final String pomLocation = "template/pom.xml";
    public static String applicationClassLocation = "template/DemoApplication.java";
    public static String controllerClassLocation = "template/TemplateController.java";
    public static String srcMainJavaLoc;
    public static String resourceLoc;
    public static final String applicationProp = "template/application.properties";
    public static String applicationClassName;
    public static String javaCodeLoc;

    @Autowired
    private AppPropertiesService appPropertiesService;
    @Autowired
    private EntityFileCreatorService entityFileCreatorService;
    @Autowired
    private ProjectCreator projectCreator;
    @Autowired
    private PomMakerService pomMakerService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private ControllerGenService controllerGenService;

    @Override
    public void createProject(Request request) throws IOException {
        logger.info("== Creating empty project ==");
        projectLocation = "/Users/User/Desktop/" + request.getProperties().getName();
        srcMainJavaLoc = projectLocation + "/src/main/java";
        resourceLoc = projectLocation + "/src/main/resources";
        projectCreator.initiliaze(request);
        projectCreator.generateEmptyProject(request);
        pomMakerService.createPomXml(request);
        projectCreator.generateMainClass(request);
        appPropertiesService.generateApplicationProperties(request);
        pomMakerService.addDependencies(request);
        databaseService.addDatabaseProps(request);
        databaseService.addDatabaseDto(request);
        entityFileCreatorService.createEntityFiles(request);
    }

    @Override
    public ByteArrayResource generateProjectZip(Long projectId) throws IOException {

        if (projectLocation == null) {
            // Gérer le cas où projectLocation est null
            throw new IllegalStateException("Project location is not initialized");
        }
        // Assuming createProject method has already been called
        Path projectPath = Paths.get(projectLocation);
        Path zipPath = zipProjectDirectory(projectPath);
        byte[] zipBytes = Files.readAllBytes(zipPath);
        return new ByteArrayResource(zipBytes);
    }
@Override
public Path zipProjectDirectory(Path projectPath) throws IOException {
        Path zipPath = Files.createTempFile("project", ".zip");
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(projectPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(projectPath.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        return zipPath;
    }
}
