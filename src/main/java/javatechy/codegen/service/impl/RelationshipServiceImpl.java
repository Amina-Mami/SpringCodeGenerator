package javatechy.codegen.service.impl;

import javatechy.codegen.dto.*;
import javatechy.codegen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    @Autowired
    private FileUtilService fileUtilService;

@Override
public String[] generateRelationshipContent(Relationship relationship, Entity sourceEntity, Entity targetEntity) {
    StringBuilder sourceSb = new StringBuilder();
    StringBuilder targetSb = new StringBuilder();

    String sourceEntityName = sourceEntity.getName();
    String targetEntityName = targetEntity.getName();
    String sourceFieldName = Character.toLowerCase(sourceEntityName.charAt(0)) + sourceEntityName.substring(1);
    String targetFieldName = Character.toLowerCase(targetEntityName.charAt(0)) + targetEntityName.substring(1);

    String direction = relationship.getDirection();
    boolean isBidirectional = "bidirectional".equalsIgnoreCase(direction);

    switch (relationship.getType()) {
        case "OneToOne":
            if (isBidirectional) {
                sourceSb.append("    @OneToOne(mappedBy = \"").append(targetFieldName).append("\")\n");
                sourceSb.append("    public ").append(targetEntityName).append(" ").append(targetFieldName).append(";\n\n");

                targetSb.append("    @OneToOne\n");
                targetSb.append("    @JoinColumn(name = \"").append(sourceFieldName).append("_id\")\n");
                targetSb.append("    public ").append(sourceEntityName).append(" ").append(sourceFieldName).append(";\n\n");
            } else {
                sourceSb.append("    @OneToOne\n");
                sourceSb.append("    @JoinColumn(name = \"").append(targetFieldName).append("_id\")\n");
                sourceSb.append("    public ").append(targetEntityName).append(" ").append(targetFieldName).append(";\n\n");
            }
            break;
        case "OneToMany":
            if (isBidirectional) {
                sourceSb.append("    @OneToMany(mappedBy = \"").append(sourceFieldName).append("\", cascade = CascadeType.ALL)\n");
                sourceSb.append("    public Set<").append(targetEntityName).append("> ").append(targetFieldName).append("s;\n\n");

                targetSb.append("    @ManyToOne\n");
                targetSb.append("    @JoinColumn(name = \"").append(sourceFieldName).append("_id\")\n");
                targetSb.append("    public ").append(sourceEntityName).append(" ").append(sourceFieldName).append(";\n\n");
            } else {
                sourceSb.append("    @OneToMany(cascade = CascadeType.ALL)\n");
                sourceSb.append("    @JoinColumn(name = \"").append(targetFieldName).append("_id\")\n");
                sourceSb.append("    public Set<").append(targetEntityName).append("> ").append(targetFieldName).append("s;\n\n");
            }
            break;
        case "ManyToOne":
            sourceSb.append("    @ManyToOne\n");
            sourceSb.append("    @JoinColumn(name = \"").append(targetFieldName).append("_id\")\n");
            sourceSb.append("    public ").append(targetEntityName).append(" ").append(targetFieldName).append(";\n\n");

            if (isBidirectional) {
                targetSb.append("    @OneToMany(mappedBy = \"").append(sourceFieldName).append("\", cascade = CascadeType.ALL)\n");
                targetSb.append("    public Set<").append(sourceEntityName).append("> ").append(sourceFieldName).append("s;\n\n");
            }
            break;
        case "ManyToMany":
            if (isBidirectional) {
                sourceSb.append("    @ManyToMany(mappedBy = \"").append(targetFieldName).append("s\")\n");
                sourceSb.append("    public Set<").append(targetEntityName).append("> ").append(targetFieldName).append("s;\n\n");

                targetSb.append("    @ManyToMany\n");
                targetSb.append("    @JoinTable(\n");
                targetSb.append("        name = \"").append(sourceFieldName).append("_").append(targetFieldName).append("\",\n");
                targetSb.append("        joinColumns = @JoinColumn(name = \"").append(sourceFieldName).append("_id\"),\n");
                targetSb.append("        inverseJoinColumns = @JoinColumn(name = \"").append(targetFieldName).append("_id\")\n");
                targetSb.append("    )\n");
                targetSb.append("    public Set<").append(sourceEntityName).append("> ").append(sourceFieldName).append("s;\n\n");
            } else {
                
                sourceSb.append("    @ManyToMany\n");
                sourceSb.append("    @JoinTable(\n");
                sourceSb.append("        name = \"").append(sourceFieldName).append("_").append(targetFieldName).append("\",\n");
                sourceSb.append("        joinColumns = @JoinColumn(name = \"").append(sourceFieldName).append("_id\"),\n");
                sourceSb.append("        inverseJoinColumns = @JoinColumn(name = \"").append(targetFieldName).append("_id\")\n");
                sourceSb.append("    )\n");
                sourceSb.append("    public Set<").append(targetEntityName).append("> ").append(targetFieldName).append("s;\n\n");
            }
            break;
    }

    return new String[] {sourceSb.toString(), targetSb.toString()};
}

}