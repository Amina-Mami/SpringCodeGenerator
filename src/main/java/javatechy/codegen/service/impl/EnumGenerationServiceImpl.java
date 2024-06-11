package javatechy.codegen.service.impl;

import javatechy.codegen.dto.EnumDefinition;
import javatechy.codegen.dto.Properties;
import javatechy.codegen.service.EnumGenerationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumGenerationServiceImpl implements EnumGenerationService {

    @Override
    public String generateEnumContent(EnumDefinition enumDefinition, Properties properties) {
        StringBuilder enumContent = new StringBuilder();


        String packageName = properties.getGroupId() + "." + properties.getArtifactId() + ".entity";
        enumContent.append("package ").append(packageName).append(";\n\n");


        enumContent.append("public enum ").append(enumDefinition.getName()).append(" {\n");


        List<String> values = enumDefinition.getValues();
        for (String value : values) {
            enumContent.append("\t").append(value).append(",\n");
        }


        enumContent.append("}");

        return enumContent.toString();
    }
}
