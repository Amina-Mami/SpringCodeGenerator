package javatechy.codegen.service.impl;

import javatechy.codegen.dto.*;
import javatechy.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    @Autowired
    private FileUtilService fileUtilService;

    @Override
    public void createRelationshipFiles(Relationship relationship, Entity entity) throws IOException {
        String relationshipContent = generateRelationshipContent(relationship, entity);
        String filePath = ProjectServiceImpl.javaCodeLoc + "/relationship/" + entity.getName() + "_" + relationship.getTargetEntity() + ".java";
        fileUtilService.writeDataToFile(relationshipContent, filePath);
    }
@Override
    public String generateRelationshipContent(Relationship relationship, Entity entity) {
        StringBuilder sb = new StringBuilder();
        String targetEntity = relationship.getTargetEntity();
        String fieldName = Character.toLowerCase(targetEntity.charAt(0)) + targetEntity.substring(1);

        switch (relationship.getType()) {
            case "OneToOne":
                sb.append("    @OneToOne\n");
                sb.append("    @JoinColumn(name = \"").append(fieldName).append("_id\")\n");
                sb.append("    private ").append(targetEntity).append(" ").append(fieldName).append(";\n\n");
                break;
            case "OneToMany":
                sb.append("    @OneToMany(mappedBy = \"").append(fieldName).append("\")\n");
                sb.append("    private List<").append(targetEntity).append("> ").append(fieldName).append("s;\n\n");
                break;
            case "ManyToOne":
                sb.append("    @ManyToOne\n");
                sb.append("    @JoinColumn(name = \"").append(fieldName).append("_id\")\n");
                sb.append("    private ").append(targetEntity).append(" ").append(fieldName).append(";\n\n");
                break;
            case "ManyToMany":
                sb.append("    @ManyToMany\n");
                sb.append("    @JoinTable(\n");
                sb.append("        name = \"").append(fieldName).append("_").append(targetEntity).append("\",\n");
                sb.append("        joinColumns = @JoinColumn(name = \"").append(fieldName).append("_id\"),\n");
                sb.append("        inverseJoinColumns = @JoinColumn(name = \"").append(targetEntity.toLowerCase()).append("_id\")\n");
                sb.append("    )\n");
                sb.append("    private List<").append(targetEntity).append("> ").append(fieldName).append("s;\n\n");
                break;
        }

        return sb.toString();
    }
}
