package javatechy.codegen.service.impl;

import javatechy.codegen.common.Common;
import javatechy.codegen.common.JacksonParser;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.FileUtilService;
import javatechy.codegen.service.SwaggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class SwaggerServiceImpl  implements SwaggerService {
    @Autowired
    private FileUtilService fileUtilService;



    @Override
    public void createSwaggerConfig(Request request) throws IOException {
        String swaggerdata = fileUtilService.getDataFromClassLoader(ProjectServiceImpl.swaggerLocation);
        Map<String, String> objectMapString = JacksonParser.jacksonObjectToMap(request.getProperties());
        swaggerdata = Common.replaceParams(swaggerdata, objectMapString);
        fileUtilService.writeDataToFile(swaggerdata, ProjectServiceImpl.javaCodeLoc + "/configurations/" + "SwaggerConfig.java");
    }
}
