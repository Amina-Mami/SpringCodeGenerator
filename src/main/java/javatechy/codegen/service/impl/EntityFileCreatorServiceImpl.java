
package javatechy.codegen.service.impl;

import javatechy.codegen.dto.*;
import javatechy.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
@Service
public class EntityFileCreatorServiceImpl implements EntityFileCreatorService {

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private EnumGenerationService enumGenerationService;

    private static final Logger logger = LogManager.getLogger(EntityFileCreatorServiceImpl.class);

    @Override
    public void createEntityFiles(Request request) throws IOException {
        Properties properties = request.getProperties();
        String groupId = properties.getGroupId();
        String artifactId = properties.getArtifactId();
        String packageLine = "package " + groupId + "." + artifactId + ".entity;\n\n";

        List<Entity> allEntities = request.getEntities();


        for (Entity entity : allEntities) {
            try {
                String entityClassContent = generateEntityClassContent(entity, packageLine, properties, allEntities);
                String entityFilePath = ProjectServiceImpl.javaCodeLoc + "/entity/" + entity.getName() + ".java";
                fileUtilService.writeDataToFile(entityClassContent, entityFilePath);

                if (Boolean.TRUE.equals(entity.getCrud())) {
                    generateRepositoryAndController(entity, properties);
                }

                for (EnumDefinition enumDefinition : entity.getEnums()) {
                    try {
                        String enumContent = enumGenerationService.generateEnumContent(enumDefinition, properties);
                        String enumFilePath = ProjectServiceImpl.javaCodeLoc + "/enums/" + enumDefinition.getName() + ".java";
                        fileUtilService.writeDataToFile(enumContent, enumFilePath);
                    } catch (IOException e) {
                        logger.error("Error writing enum file for " + enumDefinition.getName(), e);
                    }
                }
            } catch (IOException e) {
                logger.error("Error writing entity file for " + entity.getName(), e);
            }
        }


        for (Entity entity : allEntities) {
            try {
                handleRelationships(entity, allEntities, packageLine, properties);
            } catch (Exception e) {
                logger.error("Error handling relationships for entity " + entity.getName(), e);
            }
        }
    }

    private String generateEntityClassContent(Entity entity, String packageLine, Properties properties, List<Entity> allEntities) {
        StringBuilder sb = new StringBuilder();

        sb.append(packageLine);
        sb.append("import jakarta.persistence.*;\n");
        sb.append("import java.util.*;\n\n");

        Set<String> usedEnumNames = new HashSet<>();
        for (Field field : entity.getFields()) {
            if (isEnumField(field)) {
                usedEnumNames.add(field.getType());
            }
        }

        if (properties.getIsLombokEnabled()) {
            sb.append("import lombok.*;\n\n");
        }

        for (EnumDefinition enumDefinition : entity.getEnums()) {
            if (usedEnumNames.contains(enumDefinition.getName())) {
                String enumImport = properties.getGroupId() + "." + properties.getArtifactId() + ".enums." + enumDefinition.getName();
                sb.append("import ").append(enumImport).append(";\n");
            }
        }

        sb.append("@Entity\n");
        if (properties.getIsLombokEnabled()) {
            sb.append("@NoArgsConstructor\n");
            sb.append("@AllArgsConstructor\n");
            sb.append("@ToString\n");
            sb.append("@Getter\n");
            sb.append("@Setter\n");
        }
        sb.append("public class ").append(entity.getName()).append(" {\n\n");

        if (entity.getPrimaryKey() != null) {
            FieldKey primaryKeyField = entity.getPrimaryKey();
            sb.append("    @Id\n");
            sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            sb.append("    @Column(name = \"").append(primaryKeyField.getName()).append("\")\n");
            sb.append("    public ").append(primaryKeyField.getType()).append(" ").append(primaryKeyField.getName()).append(";\n\n");
        }

        for (Field field : entity.getFields()) {
            if (isEnumField(field)) {
                sb.append("    @Enumerated(EnumType.STRING)\n");
            }
            sb.append("    @Column(name = \"").append(field.getName()).append("\")\n");
            sb.append("    public ").append(getFieldType(field)).append(" ").append(field.getName()).append(";\n\n");
        }

        // Generate getters and setters if Lombok is not enabled
        if (!properties.getIsLombokEnabled()) {
            if (entity.getPrimaryKey() != null) {
                FieldKey primaryKeyField = entity.getPrimaryKey();
                sb.append(generateGetter(primaryKeyField.getType(), primaryKeyField.getName(), capitalize(primaryKeyField.getName())));
                sb.append(generateSetter(primaryKeyField.getType(), primaryKeyField.getName(), capitalize(primaryKeyField.getName())));
            }

            for (Field field : entity.getFields()) {
                String fieldType = getFieldType(field);
                String capitalizedFieldName = capitalize(field.getName());
                sb.append(generateGetter(fieldType, field.getName(), capitalizedFieldName));
                sb.append(generateSetter(fieldType, field.getName(), capitalizedFieldName));
            }
        }
        sb.append("}\n");

        return sb.toString();
    }

    private void handleRelationships(Entity entity, List<Entity> allEntities, String packageLine, Properties properties) {
        for (Relationship relationship : entity.getRelationships()) {
            Entity targetEntity = findEntityByName(relationship.getTargetEntity(), allEntities);
            if (targetEntity == null) {
                logger.error("Target entity not found for relationship: " + relationship.getTargetEntity());
                continue;
            }

            String[] relationshipContents = relationshipService.generateRelationshipContent(relationship, entity, targetEntity);

            appendRelationshipToEntity(entity, relationshipContents[0], packageLine, properties);
            appendRelationshipToEntity(targetEntity, relationshipContents[1], packageLine, properties);
        }
    }

    private void appendRelationshipToEntity(Entity entity, String relationshipContent, String packageLine, Properties properties) {
        try {
            String entityFilePath = ProjectServiceImpl.javaCodeLoc + "/entity/" + entity.getName() + ".java";
            logger.info("Reading entity file: " + entityFilePath);

            String entityClassContent = fileUtilService.readDataFromFile(entityFilePath);
            int insertIndex = entityClassContent.lastIndexOf("}");
            if (insertIndex != -1) {
                String updatedContent = new StringBuilder(entityClassContent).insert(insertIndex, relationshipContent).toString();
                fileUtilService.writeDataToFile(updatedContent, entityFilePath);
            }
        } catch (IOException e) {
            logger.error("Error reading or updating entity file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Entity findEntityByName(String name, List<Entity> allEntities) {
        for (Entity entity : allEntities) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    private boolean isEnumField(Field field) {
        List<String> basicTypes = List.of("Integer", "String", "Long", "Date", "LocalDate" ,"Boolean", "Double", "Float", "Short", "Byte", "Character");
        return !basicTypes.contains(field.getType());
    }

    private String getFieldType(Field field) {
        return field.getType();
    }

    private String generateGetter(String fieldType, String fieldName, String capitalizedFieldName) {
        return "    public " + fieldType + " get" + capitalizedFieldName + "() {\n" +
                "        return " + fieldName + ";\n" +
                "    }\n\n";
    }

    private String generateSetter(String fieldType, String fieldName, String capitalizedFieldName) {
        return "    public void set" + capitalizedFieldName + "(" + fieldType + " " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" +
                "    }\n\n";
    }





    public void generateRepositoryAndController(Entity entity, Properties properties) throws IOException {
        String repositoryContent = generateRepositoryContent(entity, properties);
        String repositoryPath = ProjectServiceImpl.javaCodeLoc + "/repository/" + entity.getName() + "Repository.java";
        fileUtilService.writeDataToFile(repositoryContent, repositoryPath);

        String serviceContent = generateServiceContent(entity, properties);
        String servicePath = ProjectServiceImpl.javaCodeLoc + "/service/" + entity.getName() + "Service.java";
        fileUtilService.writeDataToFile(serviceContent, servicePath);

        String serviceImplContent = generateServiceImplContent(entity, properties);
        String serviceImplPath = ProjectServiceImpl.javaCodeLoc + "/service/impl/" + entity.getName() + "ServiceImpl.java";
        fileUtilService.writeDataToFile(serviceImplContent, serviceImplPath);

        String controllerContent = generateControllerContent(entity, properties);
        String controllerPath = ProjectServiceImpl.javaCodeLoc + "/controller/" + entity.getName() + "Controller.java";
        fileUtilService.writeDataToFile(controllerContent, controllerPath);

        String testContent = generateControllerTestContent(entity, properties);
        String testPath = ProjectServiceImpl.testCodeLoc + "/controller/" + entity.getName() + "ControllerTest.java";
        fileUtilService.writeDataToFile(testContent, testPath);
    }

    private String generateRepositoryContent(Entity entity, Properties properties) {
        String primaryKeyType = entity.getPrimaryKey().getType();

        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".repository;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n\n" +
                "public interface " + entity.getName() + "Repository extends JpaRepository<" + entity.getName() + ", " + primaryKeyType + "> {\n" +
                "}\n";
    }


    private String generateServiceContent(Entity entity, Properties properties) {
        String primaryKeyType = entity.getPrimaryKey().getType();
        String primaryKeyName = entity.getPrimaryKey().getName();

        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".service;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import java.util.List;\n" +
                "import java.util.Optional;\n\n" +
                "public interface " + entity.getName() + "Service {\n" +
                "    " + entity.getName() + " save(" + entity.getName() + " " + entity.getName().toLowerCase() + ");\n" +
                "    List<" + entity.getName() + "> findAll();\n" +
                "    Optional<" + entity.getName() + "> findById(" + primaryKeyType + " " + primaryKeyName + ");\n" +
                "    void deleteById(" + primaryKeyType + " " + primaryKeyName + ");\n" +
                "    " + entity.getName() + " update(" + primaryKeyType + " " + primaryKeyName + ", " + entity.getName() + " updated" + entity.getName() + ");\n" +
                "}\n";
    }




    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String generateServiceImplContent(Entity entity, Properties properties) {
        String primaryKeyType = entity.getPrimaryKey().getType();
        String primaryKeyName = entity.getPrimaryKey().getName();

        StringBuilder updateFields = new StringBuilder();
        for (Field field : entity.getFields()) {
            String fieldName = field.getName();
            updateFields.append("            entityToUpdate.set")
                    .append(capitalize(fieldName))
                    .append("(updated")
                    .append(entity.getName())
                    .append(".get")
                    .append(capitalize(fieldName))
                    .append("());\n");
        }

        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".service.impl;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".repository." + entity.getName() + "Repository;\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".service." + entity.getName() + "Service;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import java.util.List;\n" +
                "import java.util.Optional;\n" +
                "import java.util.NoSuchElementException;\n\n" +
                "@Service\n" +
                "public class " + entity.getName() + "ServiceImpl implements " + entity.getName() + "Service {\n\n" +
                "    @Autowired\n" +
                "    private " + entity.getName() + "Repository " + entity.getName().toLowerCase() + "Repository;\n\n" +
                "    @Override\n" +
                "    public " + entity.getName() + " save(" + entity.getName() + " " + entity.getName().toLowerCase() + ") {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.save(" + entity.getName().toLowerCase() + ");\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public List<" + entity.getName() + "> findAll() {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.findAll();\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Optional<" + entity.getName() + "> findById(" + primaryKeyType + " " + primaryKeyName + ") {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.findById(" + primaryKeyName + ");\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public void deleteById(" + primaryKeyType + " " + primaryKeyName + ") {\n" +
                "        " + entity.getName().toLowerCase() + "Repository.deleteById(" + primaryKeyName + ");\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + entity.getName() + " update(" + primaryKeyType + " " + primaryKeyName + ", " + entity.getName() + " updated" + entity.getName() + ") {\n" +
                "        Optional<" + entity.getName() + "> existingEntity = " + entity.getName().toLowerCase() + "Repository.findById(" + primaryKeyName + ");\n" +
                "        if (existingEntity.isPresent()) {\n" +
                "            " + entity.getName() + " entityToUpdate = existingEntity.get();\n" +
                updateFields.toString() +
                "            return " + entity.getName().toLowerCase() + "Repository.save(entityToUpdate);\n" +
                "        } else {\n" +
                "            throw new NoSuchElementException(\"" + entity.getName() + " not found with id \" + " + primaryKeyName + ");\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
    }




    private String generateControllerContent(Entity entity, Properties properties) {
        String primaryKeyType = entity.getPrimaryKey().getType();
        String primaryKeyParam = entity.getPrimaryKey().getName();

        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".controller;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".service." + entity.getName() + "Service;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import java.util.NoSuchElementException;\n" +
                "import java.util.Optional;\n" +
                "import java.util.List;\n\n" +
                "@RestController\n" +
                "@CrossOrigin(origins = \"*\")\n" +
                "public class " + entity.getName() + "Controller {\n\n" +
                "    @Autowired\n" +
                "    private " + entity.getName() + "Service " + entity.getName().toLowerCase() + "Service;\n\n" +
                "    @PostMapping(\"/" + entity.getName().toLowerCase() + "\")\n" +
                "    public " + entity.getName() + " new" + entity.getName() + "(@RequestBody " + entity.getName() + " new" + entity.getName() + ") {\n" +
                "        return " + entity.getName().toLowerCase() + "Service.save(new" + entity.getName() + ");\n" +
                "    }\n\n" +
                "    @GetMapping(\"/get\")\n" +
                "    public ResponseEntity<" + entity.getName() + "> getById(@RequestParam(\"" + primaryKeyParam + "\") " + primaryKeyType + " " + primaryKeyParam + ") {\n" +
                "        Optional<" + entity.getName() + "> optionalEntity = " + entity.getName().toLowerCase() + "Service.findById(" + primaryKeyParam + ");\n" +
                "        return optionalEntity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));\n" +
                "    }\n\n" +
                "    @DeleteMapping(\"/" + entity.getName().toLowerCase() + "\")\n" +
                "    public void delete" + entity.getName() + "(@RequestParam(\"" + primaryKeyParam + "\") " + primaryKeyType + " " + primaryKeyParam + ") {\n" +
                "        " + entity.getName().toLowerCase() + "Service.deleteById(" + primaryKeyParam + ");\n" +
                "    }\n\n" +
                "    @PutMapping(\"/update\")\n" +
                "    public ResponseEntity<" + entity.getName() + "> update" + entity.getName() + "(@RequestParam(\"" + primaryKeyParam + "\") " + primaryKeyType + " " + primaryKeyParam + ", @RequestBody " + entity.getName() + " updated" + entity.getName() + ") {\n" +
                "        try {\n" +
                "            " + entity.getName() + " updatedEntity = " + entity.getName().toLowerCase() + "Service.update(" + primaryKeyParam + ", updated" + entity.getName() + ");\n" +
                "            return ResponseEntity.ok(updatedEntity);\n" +
                "        } catch (NoSuchElementException e) {\n" +
                "            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
    }


private String generateControllerTestContent(Entity entity, Properties properties) {
    String entityName = entity.getName();
    String entityVar = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    String primaryKeyType = entity.getPrimaryKey().getType();
    String primaryKeyFieldName = entity.getPrimaryKey().getName();

    StringBuilder content = new StringBuilder();

    content.append("package ").append(properties.getGroupId()).append(".").append(properties.getArtifactId()).append(".controller;\n\n");
    content.append("import ").append(properties.getGroupId()).append(".").append(properties.getArtifactId()).append(".entity.").append(entityName).append(";\n");
    content.append("import ").append(properties.getGroupId()).append(".").append(properties.getArtifactId()).append(".repository.").append(entityName).append("Repository;\n");
    content.append("import org.junit.jupiter.api.Test;\n");
    content.append("import org.junit.jupiter.api.extension.ExtendWith;\n");
    content.append("import org.mockito.Mockito;\n");
    content.append("import org.springframework.beans.factory.annotation.Autowired;\n");
    content.append("import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\n");
    content.append("import org.springframework.boot.test.mock.mockito.MockBean;\n");
    content.append("import org.springframework.http.MediaType;\n");
    content.append("import org.springframework.test.context.junit.jupiter.SpringExtension;\n");
    content.append("import org.springframework.test.web.servlet.MockMvc;\n");
    content.append("import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;\n\n");
    content.append("import java.util.Collections;\n");
    content.append("import java.util.Optional;\n\n");
    content.append("import static org.hamcrest.Matchers.is;\n");
    content.append("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;\n\n");
    content.append("@ExtendWith(SpringExtension.class)\n");
    content.append("@WebMvcTest(").append(entityName).append("Controller.class)\n");
    content.append("public class ").append(entityName).append("ControllerTest {\n\n");

    content.append("    @Autowired\n");
    content.append("    private MockMvc mockMvc;\n\n");
    content.append("    @MockBean\n");
    content.append("    private ").append(entityName).append("Repository ").append(entityVar).append("Repository;\n\n");

    content.append(generateCreateMethod(entity, entityVar, primaryKeyType, primaryKeyFieldName));
    content.append(generateGetAllMethod(entity, entityVar, primaryKeyType, primaryKeyFieldName));
    content.append(generateGetMethod(entity, entityVar, primaryKeyType, primaryKeyFieldName));
    content.append(generateUpdateMethod(entity, entityVar, primaryKeyType, primaryKeyFieldName));
    content.append(generateDeleteMethod(entity, entityVar, primaryKeyType, primaryKeyFieldName));

    content.append("}\n");

    return content.toString();
}

    private String generateCreateMethod(Entity entity, String entityVar, String primaryKeyType, String primaryKeyFieldName) {
        String entityName = entity.getName();
        StringBuilder method = new StringBuilder();

        method.append("    @Test\n");
        method.append("    public void testCreate").append(entityName).append("() throws Exception {\n");
        method.append("        ").append(entityName).append(" ").append(entityVar).append(" = new ").append(entityName).append("();\n");

        method.append("        ").append(entityVar).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        Mockito.when(").append(entityVar).append("Repository.save(Mockito.any(").append(entityName).append(".class))).thenReturn(").append(entityVar).append(");\n\n");

        method.append("        mockMvc.perform(MockMvcRequestBuilders.post(\"/").append(entityVar).append("\")\n");
        method.append("                .contentType(MediaType.APPLICATION_JSON)\n");
        method.append("                .content(\"{\\\"").append(primaryKeyFieldName).append("\\\": ").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append("}\"))\n");
        method.append("                .andExpect(status().isOk())\n");
        method.append("                .andExpect(jsonPath(\"$.").append(primaryKeyFieldName).append("\", is(").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append(")));\n");
        method.append("    }\n\n");

        return method.toString();
    }

    private String generateGetAllMethod(Entity entity, String entityVar, String primaryKeyType, String primaryKeyFieldName) {
        String entityName = entity.getName();
        StringBuilder method = new StringBuilder();

        method.append("    @Test\n");
        method.append("    public void testGetAll").append(entityName).append("s() throws Exception {\n");
        method.append("        ").append(entityName).append(" ").append(entityVar).append(" = new ").append(entityName).append("();\n");

        method.append("        ").append(entityVar).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        Mockito.when(").append(entityVar).append("Repository.findAll()).thenReturn(Collections.singletonList(").append(entityVar).append("));\n\n");

        method.append("        mockMvc.perform(MockMvcRequestBuilders.get(\"/").append(entityVar).append("s\"))\n");
        method.append("                .andExpect(status().isOk())\n");
        method.append("                .andExpect(jsonPath(\"$[0].").append(primaryKeyFieldName).append("\", is(").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append(")));\n");
        method.append("    }\n\n");

        return method.toString();
    }

    private String generateGetMethod(Entity entity, String entityVar, String primaryKeyType, String primaryKeyFieldName) {
        String entityName = entity.getName();
        StringBuilder method = new StringBuilder();

        method.append("    @Test\n");
        method.append("    public void testGet").append(entityName).append("ById() throws Exception {\n");
        method.append("        ").append(entityName).append(" ").append(entityVar).append(" = new ").append(entityName).append("();\n");
        method.append("        ").append(entityVar).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        Mockito.when(").append(entityVar).append("Repository.findById(").append(primaryKeyType.equals("Long") ? getPrimaryKeySampleValue(primaryKeyType) + "L" : getPrimaryKeySampleValue(primaryKeyType)).append(")).thenReturn(Optional.of(").append(entityVar).append("));\n\n");

        method.append("        mockMvc.perform(MockMvcRequestBuilders.get(\"/").append(entityVar).append("/").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append("\"))\n");
        method.append("                .andExpect(status().isOk())\n");
        method.append("                .andExpect(jsonPath(\"$.").append(primaryKeyFieldName).append("\", is(").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append(")));\n");
        method.append("    }\n\n");

        return method.toString();
    }

    private String generateUpdateMethod(Entity entity, String entityVar, String primaryKeyType, String primaryKeyFieldName) {
        String entityName = entity.getName();
        StringBuilder method = new StringBuilder();

        method.append("    @Test\n");
        method.append("    public void testUpdate").append(entityName).append("() throws Exception {\n");
        method.append("        ").append(entityName).append(" existing").append(entityName).append(" = new ").append(entityName).append("();\n");
        method.append("        existing").append(entityName).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        ").append(entityName).append(" updated").append(entityName).append(" = new ").append(entityName).append("();\n");
        method.append("        updated").append(entityName).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        Mockito.when(").append(entityVar).append("Repository.findById(").append(getPrimaryKeySampleValue(primaryKeyType)).append(")).thenReturn(Optional.of(existing").append(entityName).append("));\n");
        method.append("        Mockito.when(").append(entityVar).append("Repository.save(Mockito.any(").append(entityName).append(".class))).thenReturn(updated").append(entityName).append(");\n\n");

        method.append("        mockMvc.perform(MockMvcRequestBuilders.put(\"/").append(entityVar).append("/").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append("\")\n");
        method.append("                .contentType(MediaType.APPLICATION_JSON)\n");
        method.append("                .content(\"{\\\"").append(primaryKeyFieldName).append("\\\": ").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append("}\"))\n");
        method.append("                .andExpect(status().isOk())\n");
        method.append("                .andExpect(jsonPath(\"$.").append(primaryKeyFieldName).append("\", is(").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append(")));\n");
        method.append("    }\n\n");

        return method.toString();
    }

    private String generateDeleteMethod(Entity entity, String entityVar, String primaryKeyType, String primaryKeyFieldName) {
        String entityName = entity.getName();
        StringBuilder method = new StringBuilder();

        method.append("    @Test\n");
        method.append("    public void testDelete").append(entityName).append("() throws Exception {\n");
        method.append("        ").append(entityName).append(" ").append(entityVar).append(" = new ").append(entityName).append("();\n");
        method.append("        ").append(entityVar).append(".set").append(primaryKeyFieldName.substring(0, 1).toUpperCase()).append(primaryKeyFieldName.substring(1)).append("(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n");

        method.append("        Mockito.when(").append(entityVar).append("Repository.findById(").append(getPrimaryKeySampleValue(primaryKeyType)).append(")).thenReturn(Optional.of(").append(entityVar).append("));\n");
        method.append("        Mockito.doNothing().when(").append(entityVar).append("Repository).deleteById(").append(getPrimaryKeySampleValue(primaryKeyType)).append(");\n\n");

        method.append("        mockMvc.perform(MockMvcRequestBuilders.delete(\"/").append(entityVar).append("/").append(getPrimaryKeySampleValueAsString(primaryKeyType)).append("\"))\n");
        method.append("                .andExpect(status().isOk());\n");
        method.append("    }\n\n");

        return method.toString();
    }

    private String getPrimaryKeySampleValue(String primaryKeyType) {
        switch (primaryKeyType) {
            case "Long":
                return "1L";
            case "Integer":
            case "int":
                return "1";
            case "String":
                return "\"1\"";
            default:
                return "1";
        }
    }


    private String getPrimaryKeySampleValueAsString(String primaryKeyType) {
        switch (primaryKeyType) {
            case "Long":
                return "1";
            case "Integer":
            case "int":
                return "1";
            case "String":
                return "\"1\"";
            default:
                return "1";
        }
    }



}

