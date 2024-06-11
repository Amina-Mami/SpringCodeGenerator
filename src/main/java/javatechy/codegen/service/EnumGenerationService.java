package javatechy.codegen.service;


import javatechy.codegen.dto.EnumDefinition;
import javatechy.codegen.dto.Field;
import javatechy.codegen.dto.Properties;

public interface EnumGenerationService {




    String generateEnumContent(EnumDefinition enumDefinition, Properties properties);
}
