package javatechy.codegen.service.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javatechy.codegen.common.Common;
import javatechy.codegen.common.JacksonParser;
import javatechy.codegen.dto.Properties;
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
        logger.info("javaCodeLoc => " + ProjectServiceImpl.javaCodeLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.srcMainJavaLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.resourceLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc);
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/service/impl");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/controller");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/exception");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/dao");
        fileUtilService.createDirectories(ProjectServiceImpl.javaCodeLoc + "/entity");
    }

    @Override
    public void initiliaze(Request request) {
        Properties properties = request.getProperties();

        // Transform groupId and artifactId into a package name
        String basePackage = properties.getGroupId() + "." + properties.getArtifactId();
        basePackage = basePackage.toLowerCase().replace("-", "").replace("_", ""); // Ensure it's a valid Java package name

        // Set the application class name with proper CamelCase convention
        String applicationClassName = Common.toCamelCase(properties.getArtifactId()) + "Application";
        ProjectServiceImpl.applicationClassName = applicationClassName;
        logger.info("Application Class Name => " + applicationClassName);

        // Store full package path in properties for later use
        properties.setApplicationClassName(applicationClassName);
        properties.setBasePackage(basePackage);  // Assuming you have a setter for this

        // Update paths based on the base package
        ProjectServiceImpl.javaCodeLoc = ProjectServiceImpl.srcMainJavaLoc + "/" + basePackage.replace(".", "/");
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

    @Override
    public void generateMainClass(Request request) throws IOException {
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
        String applicationClassData = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.applicationClassLocation);
        applicationClassData = Common.replaceParams(applicationClassData, objectMapString);
        fileUtilService.writeDataToFile(applicationClassData, ProjectServiceImpl.javaCodeLoc + "/" + ProjectServiceImpl.applicationClassName + ".java");

    }
}
