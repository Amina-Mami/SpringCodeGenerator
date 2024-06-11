package javatechy.codegen.service.impl;

import java.io.IOException;
import java.util.Map;

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

    @Override
    public void generateApplicationProperties(Request request) throws IOException {
        logger.info("Creating application properties => ");
        String applicationPropertiesData = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.applicationProp);
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
        applicationPropertiesData = Common.replaceParams(applicationPropertiesData, objectMapString);
        fileUtilService.writeDataToFile(applicationPropertiesData, ProjectServiceImpl.resourceLoc + "/" + "application.properties");
    }
}