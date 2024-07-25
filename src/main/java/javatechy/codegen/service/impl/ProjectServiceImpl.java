package javatechy.codegen.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javatechy.codegen.dto.JsonFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javatechy.codegen.controller.CodeGenController;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger logger = Logger.getLogger(CodeGenController.class);
    public static String projectLocation;
    public static final String pomLocation = "template/pom.xml";
    public static final String swaggerLocation = "template/SwaggerConfigTemplate.java";
    public static String applicationClassLocation = "template/DemoApplication.java";
    public static String controllerClassLocation = "template/TemplateController.java";
    public static String srcMainJavaLoc;
    public static String srcTestJavaLoc;
    public static String resourceLoc;
    public static final String applicationProp = "template/application.properties";
    public static String applicationClassName;
    public static String javaCodeLoc;
    public static String testCodeLoc;
    private static final String PROJECT_DIRECTORY = "C:\\Users\\User\\Desktop\\projects\\";

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
    @Autowired
    private SwaggerService swaggerService;

    @Override
    public void createProject(Request request, Path projectDirectory) throws IOException {
        logger.info("== Creating empty project ==");
        projectLocation = projectDirectory.toString();
        srcMainJavaLoc = projectLocation + "/src/main/java";
        srcTestJavaLoc = projectLocation + "/src/test/java";
        resourceLoc = projectLocation + "/src/main/resources";
        projectCreator.initiliaze(request);
        projectCreator.generateEmptyProject(request);
        pomMakerService.createPomXml(request);
        swaggerService.createSwaggerConfig(request);
        projectCreator.generateMainClass(request);
        appPropertiesService.generateApplicationProperties(request);
        pomMakerService.addDependencies(request);
        databaseService.addDatabaseProps(request);
        databaseService.addDatabaseDto(request);
        entityFileCreatorService.createEntityFiles(request);
    }




}
