
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

        // Create all entities first
        for (Entity entity : allEntities) {
            String entityClassContent = generateEntityClassContent(entity, packageLine, properties, allEntities);

            String entityFilePath = ProjectServiceImpl.javaCodeLoc + "/entity/" + entity.getName() + ".java";
            fileUtilService.writeDataToFile(entityClassContent, entityFilePath);

            if (entity.getCrud()) {
                generateRepositoryAndController(entity, properties);
            }

            for (EnumDefinition enumDefinition : entity.getEnums()) {
                String enumContent = enumGenerationService.generateEnumContent(enumDefinition, properties);
                String enumFilePath = ProjectServiceImpl.javaCodeLoc + "/enums/" + enumDefinition.getName() + ".java";
                fileUtilService.writeDataToFile(enumContent, enumFilePath);
            }
        }

        // After all entities are created, handle relationships
        for (Entity entity : allEntities) {
            handleRelationships(entity, allEntities, packageLine, properties);
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
        // Generate imports only for the enums that are used
        for (EnumDefinition enumDefinition : entity.getEnums()) {
            if (usedEnumNames.contains(enumDefinition.getName())) {
                String enumImport = properties.getGroupId() + "." + properties.getArtifactId() + ".enums." + enumDefinition.getName();
                sb.append("import ").append(enumImport).append(";\n");
            }
        }

        sb.append("@Entity\n");
        // Conditionally add Lombok annotations only if Lombok is enabled
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
                continue; // Skip this relationship if the target entity is not found
            }
            String[] relationshipContents = relationshipService.generateRelationshipContent(relationship, entity, targetEntity);

            appendRelationshipToTargetEntity(targetEntity, relationshipContents[1], packageLine, properties);
        }
    }

    private void appendRelationshipToTargetEntity(Entity targetEntity, String relationshipContent, String packageLine, Properties properties) {
        try {
            String targetEntityFilePath = ProjectServiceImpl.javaCodeLoc + "/entity/" + targetEntity.getName() + ".java";
            logger.info("Reading target entity file: " + targetEntityFilePath);

            String targetEntityClassContent = fileUtilService.readDataFromFile(targetEntityFilePath);
            int insertIndex = targetEntityClassContent.lastIndexOf("}");
            if (insertIndex != -1) {
                String updatedContent = new StringBuilder(targetEntityClassContent).insert(insertIndex, relationshipContent).toString();
                fileUtilService.writeDataToFile(updatedContent, targetEntityFilePath);
            }
        } catch (IOException e) {
            logger.error("Error reading or updating target entity file: " + e.getMessage());
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
        List<String> basicTypes = List.of("Integer", "String", "Long", "Boolean", "Double", "Float", "Short", "Byte", "Character");
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
    private void generateRepositoryAndController(Entity entity, Properties properties) throws IOException {
        String repositoryContent = generateRepositoryContent(entity, properties);
        String repositoryPath = ProjectServiceImpl.javaCodeLoc + "/repository/" + entity.getName() + "Repository.java";
        fileUtilService.writeDataToFile(repositoryContent, repositoryPath);

        String controllerContent = generateControllerContent(entity, properties);
        String controllerPath = ProjectServiceImpl.javaCodeLoc + "/controller/" + entity.getName() + "Controller.java";
        fileUtilService.writeDataToFile(controllerContent, controllerPath);
    }

    private String generateRepositoryContent(Entity entity, Properties properties) {
        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".repository;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n\n" +
                "public interface " + entity.getName() + "Repository extends JpaRepository<" + entity.getName() + ", Long> {\n" +
                "}\n";
    }

    private String generateControllerContent(Entity entity, Properties properties) {
        String primaryKeyType = entity.getPrimaryKey().getType();

        return "package " + properties.getGroupId() + "." + properties.getArtifactId() + ".controller;\n\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".entity." + entity.getName() + ";\n" +
                "import " + properties.getGroupId() + "." + properties.getArtifactId() + ".repository." + entity.getName() + "Repository;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.web.bind.annotation.*;\n\n" +
                "import java.util.List;\n\n" +
                "@RestController\n" +
                "@CrossOrigin(origins = \"*\")\n" +
                "public class " + entity.getName() + "Controller {\n\n" +
                "    @Autowired\n" +
                "    private " + entity.getName() + "Repository " + entity.getName().toLowerCase() + "Repository;\n\n" +
                "    @PostMapping(\"/" + entity.getName().toLowerCase() + "\")\n" +
                "    public " + entity.getName() + " new" + entity.getName() + "(@RequestBody " + entity.getName() + " new" + entity.getName() + ") {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.save(new" + entity.getName() + ");\n" +
                "    }\n\n" +
                "    @GetMapping(\"/" + entity.getName().toLowerCase() + "s\")\n" +
                "    public List<" + entity.getName() + "> getAll" + entity.getName() + "s() {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.findAll();\n" +
                "    }\n\n" +
                "    @GetMapping(\"/" + entity.getName().toLowerCase() + "\")\n" +
                "    public " + entity.getName() + " getPersonne(@RequestParam Long id) {\n" +
                "        return " + entity.getName().toLowerCase() + "Repository.findById(id).orElse(null);\n" +
                "    }\n\n" +
                "    @PutMapping(\"/" + entity.getName().toLowerCase() + "/{id}\")\n" +
                "    public " + entity.getName() + " update" + entity.getName() + "(@PathVariable " + primaryKeyType + " id, @RequestBody " + entity.getName() + " updated" + entity.getName() + ") {\n" +
                "        updated" + entity.getName() + ".set" + entity.getPrimaryKey().getName().substring(0, 1).toUpperCase() + entity.getPrimaryKey().getName().substring(1) + "(id); \n" +
                "        return " + entity.getName().toLowerCase() + "Repository.save(updated" + entity.getName() + ");\n" +
                "    }\n\n" +
                "    @DeleteMapping(\"/" + entity.getName().toLowerCase() + "/{id}\")\n" +
                "    public void delete" + entity.getName() + "(@PathVariable " + primaryKeyType + " id) {\n" +
                "        " + entity.getName().toLowerCase() + "Repository.deleteById(id);\n" +
                "    }\n\n" +
                "    // Additional CRUD operations here\n" +
                "}\n";
    }
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
