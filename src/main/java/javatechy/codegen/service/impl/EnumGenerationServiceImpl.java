package javatechy.codegen.service.impl;

import javatechy.codegen.dto.EnumDefinition;
import javatechy.codegen.dto.Properties;
import javatechy.codegen.dto.Values;
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

        List<Values> values = enumDefinition.getValues();
        for (Values value : values) {
            enumContent.append("\t").append(value.getValues()).append(",\n");
        }


        int lastIndex = enumContent.lastIndexOf(",");
        if (lastIndex != -1) {
            enumContent.deleteCharAt(lastIndex);
        }

        enumContent.append("\n}");

        return enumContent.toString();
    }
}
