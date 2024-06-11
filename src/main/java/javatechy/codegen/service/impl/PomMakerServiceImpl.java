package javatechy.codegen.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javatechy.codegen.common.Common;
import javatechy.codegen.common.JacksonParser;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.FileUtilService;
import javatechy.codegen.service.PomMakerService;

@Service
public class PomMakerServiceImpl implements PomMakerService {

    @Autowired
    private FileUtilService fileUtilService;

    @Override
    public void createPomXml(Request request) throws IOException {
        String pomXmlData = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.pomLocation);
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
        pomXmlData = Common.replaceParams(pomXmlData, objectMapString);
        fileUtilService.writeDataToFile(pomXmlData, ProjectServiceImpl.projectLocation + "/" + "pom.xml");
    }

    @Override
    public void addDependencies(Request request) {
        // TODO Auto-generated method stub

    }

}
