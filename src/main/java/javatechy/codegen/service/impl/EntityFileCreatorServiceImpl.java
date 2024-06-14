package javatechy.codegen.service.impl;

import javatechy.codegen.dto.*;
import javatechy.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EntityFileCreatorServiceImpl implements EntityFileCreatorService {

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private EnumGenerationService enumGenerationService;


    @Override
    public void createEntityFiles(Request request) throws IOException {
        Properties properties = request.getProperties();
        String groupId = properties.getGroupId();
        String artifactId = properties.getArtifactId();
        String packageLine = "package " + groupId + "." + artifactId + ".entity;\n\n";

        for (Entity entity : request.getEntities()) {
            String entityClassContent = generateEntityClassContent(entity, packageLine, properties);

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

            for (Relationship relationship : entity.getRelationships()) {
                relationshipService.createRelationshipFiles(relationship, entity);
            }
        }
    }


    public String generateEntityClassContent(Entity entity, String packageLine, Properties properties) {
        StringBuilder sb = new StringBuilder();

        sb.append(packageLine);
        sb.append("import jakarta.persistence.*;\n");
        sb.append("import java.util.List;\n\n");
        sb.append("import lombok.*;\n\n");

        for (EnumDefinition enumDefinition : entity.getEnums()) {
            String enumImport = properties.getGroupId() + "." + properties.getArtifactId() + ".enums." + enumDefinition.getName();
            sb.append("import ").append(enumImport).append(";\n");
        }

        sb.append("@Entity\n");


        sb.append("@NoArgsConstructor\n");
        sb.append("@AllArgsConstructor\n");
        sb.append("@ToString\n");
        sb.append("@Getter\n");
        sb.append("@Setter\n");

        sb.append("public class ").append(entity.getName()).append(" {\n\n");

        if (entity.getPrimaryKey() != null) {
            FieldKey primaryKeyField = entity.getPrimaryKey();
            sb.append("    @Id\n");
            sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            sb.append("    @Column(name = \"").append(primaryKeyField.getName()).append("\")\n");
            sb.append("    private ").append(primaryKeyField.getType()).append(" ").append(primaryKeyField.getName()).append(";\n\n");
        }

        for (Field field : entity.getFields()) {
            if (isEnumField(field)) {
                sb.append("    @Enumerated(EnumType.STRING)\n");
            }

            sb.append("    @Column(name = \"").append(field.getName()).append("\")\n");
            sb.append("    private ").append(getFieldType(field)).append(" ").append(field.getName()).append(";\n\n");
        }

        for (Relationship relationship : entity.getRelationships()) {
            sb.append(relationshipService.generateRelationshipContent(relationship, entity)).append("\n");
        }



        sb.append("}\n");

        return sb.toString();
    }













    private boolean isEnumField(Field field) {

        List<String> basicTypes = List.of("Integer", "String", "Long", "Boolean", "Double", "Float", "Short", "Byte", "Character");

        return !basicTypes.contains(field.getType());
    }

    private String getFieldType(Field field) {

        return field.getType();
    }




   /* private String generateGetter(String fieldType, String fieldName, String capitalizedFieldName) {
        return "    public " + fieldType + " get" + capitalizedFieldName + "() {\n" +
                "        return " + fieldName + ";\n" +
                "    }\n\n";
    }

    private String generateSetter(String fieldType, String fieldName, String capitalizedFieldName) {
        return "    public void set" + capitalizedFieldName + "(" + fieldType + " " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" +
                "    }\n\n";
    } */

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



    /*private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }*/
}

