package javatechy.codegen.service.impl;

import java.io.IOException;
import java.util.Map;

import javatechy.codegen.dto.Database;
import javatechy.codegen.dto.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javatechy.codegen.common.Common;
import javatechy.codegen.common.JacksonParser;
import javatechy.codegen.controller.CodeGenController;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.AppPropertiesService;
import javatechy.codegen.service.FileUtilService;

@Service
public class AppPropertiesServiceImpl implements AppPropertiesService {
    private final Logger logger = Logger.getLogger(CodeGenController.class);

    @Autowired
    private FileUtilService fileUtilService;

//    @Override
//    public void generateApplicationProperties(Request request) throws IOException {
//        logger.info("Creating application properties => ");
//        String applicationPropertiesData = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.applicationProp);
//        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
//        applicationPropertiesData = Common.replaceParams(applicationPropertiesData, objectMapString);
//        fileUtilService.writeDataToFile(applicationPropertiesData, ProjectServiceImpl.resourceLoc + "/" + "application.properties");
//    }

    @Override
    public void generateApplicationProperties(Request request) throws IOException {
        logger.info("Creating application properties => ");

        String activeProfile = determineActiveProfile(request);
        String applicationPropertiesData = fileUtilService.getDataFromClassLoader("template/application-" + activeProfile + ".properties");

        Properties properties = request.getProperties();
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(properties);

        Database database = request.getDatabase();
        if (database != null && Boolean.TRUE.equals(database.getDatabaseEnabled())) {
            objectMapString.put("port", String.valueOf(database.getPort()));
            objectMapString.put("databaseName", database.getDatabaseName());
            objectMapString.put("userName", database.getUserName());
            objectMapString.put("password", database.getPassword());
        }

        applicationPropertiesData = Common.replaceParams(applicationPropertiesData, objectMapString);

        logger.debug("Generated application properties data:");
        logger.debug(applicationPropertiesData);

        String resourceLocation = ProjectServiceImpl.resourceLoc + "/application.properties";
        fileUtilService.writeDataToFile(applicationPropertiesData, resourceLocation);
    }

    private String determineActiveProfile(Request request) {
        Database database = request.getDatabase();
        if (database != null && Boolean.TRUE.equals(database.getDatabaseEnabled())) {
            String dbType = database.getDatabaseType();
            if ("mysql".equalsIgnoreCase(dbType)) {
                return "mysql";
            } else if ("postgresql".equalsIgnoreCase(dbType)) {
                return "postgresql";
            } else if ("mongodb".equalsIgnoreCase(dbType)) {
                return "mongodb";
            }
        }
        return "default";
    }

}