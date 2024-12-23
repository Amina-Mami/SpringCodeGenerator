package javatechy.codegen.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javatechy.codegen.common.JacksonParser;
import javatechy.codegen.dto.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javatechy.codegen.common.Common;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.FileUtilService;
import javatechy.codegen.service.ProjectCreator;

@Service
public class ProjectCreatorImpl implements ProjectCreator {

    private Logger logger = Logger.getLogger(ProjectCreatorImpl.class);

    @Autowired
    private FileUtilService fileUtilService;

    @Override
    public void generateEmptyProject(Request request) throws IOException {
        logger.info("javaCodeLoc => " + ProjectServiceImpl.javaCodeLoc);
        ProjectServiceImpl.javaCodeLoc = getJavaCodeLoc(request);
        ProjectServiceImpl.testCodeLoc = getTestCodeLoc(request);
        logger.info("javaCodeLoc => " + ProjectServiceImpl.javaCodeLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.srcMainJavaLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.srcTestJavaLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.resourceLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.testCodeLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/service/impl");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/controller");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/exception");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/dao");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/entity");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/configurations");
        createDockerFile(request.getProperties(), ProjectServiceImpl.projectLocation);
    }


    @Override
    public void initiliaze(Request request) {
        Properties properties = request.getProperties();


        String basePackage = properties.getGroupId() + "." + properties.getArtifactId();
        basePackage = basePackage.toLowerCase().replace("-", "").replace("_", ""); // Ensure it's a valid Java package name


        String applicationClassName = Common.toCamelCase(properties.getArtifactId()) + "Application";
        ProjectServiceImpl.applicationClassName = applicationClassName;
        logger.info("Application Class Name => " + applicationClassName);


        properties.setApplicationClassName(applicationClassName);
        properties.setBasePackage(basePackage);


        ProjectServiceImpl.javaCodeLoc = ProjectServiceImpl.srcMainJavaLoc + "/" + basePackage.replace(".", "/");
        ProjectServiceImpl.testCodeLoc = ProjectServiceImpl.srcTestJavaLoc + "/" + basePackage.replace(".", "/");
    }

    /**
     *  mkdir src/main/java && src/main/resources
     *  add pom.xml in project folder && add README.md in project folder &&  add .gitignore in the project folder
     */
    private String getJavaCodeLoc(Request request) {
        return (ProjectServiceImpl.srcMainJavaLoc + "." + request.getProperties()
                .getGroupId() + "."
                + request.getProperties()
                .getArtifactId()).replaceAll("\\.", "/");
    }

    /**
     *  mkdir src/test/java
     */
    private String getTestCodeLoc(Request request) {
        return (ProjectServiceImpl.srcTestJavaLoc + "." + request.getProperties()
                .getGroupId() + "."
                + request.getProperties()
                .getArtifactId()).replaceAll("\\.", "/");
    }

    @Override
    public void generateMainClass(Request request) throws IOException {
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
        String applicationClassData = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.applicationClassLocation);
        applicationClassData = Common.replaceParams(applicationClassData, objectMapString);
        fileUtilService.writeDataToFile(applicationClassData, ProjectServiceImpl.javaCodeLoc + "/" + ProjectServiceImpl.applicationClassName + ".java");

    }
    private void createDockerFile(Properties properties, String projectPath) {
        if (properties.getIsDockerEnabled()) {
            String dockerFileContent = "FROM openjdk:11-jre-slim\n" +
                    "VOLUME /tmp\n" +
                    "COPY target/" + properties.getArtifactId() + "-0.0.1-SNAPSHOT.jar app.jar\n" +
                    "ENTRYPOINT [\"java\", \"-Djava.security.egd=file:/dev/./urandom\", \"-jar\", \"/app.jar\"]";

            try {
                Files.write(Paths.get(projectPath + "/Dockerfile"), dockerFileContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
