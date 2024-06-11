package javatechy.codegen.service;

import javatechy.codegen.dto.Entity;
import javatechy.codegen.dto.Relationship;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RelationshipService {


    void createRelationshipFiles(Relationship relationship, Entity entity) throws IOException;

    String generateRelationshipContent(Relationship relationship, Entity entity);
}

