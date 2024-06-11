package ${groupId}.${artifactId}.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javatechy.codegen.dto.Request;
import javatechy.codegen.dto.Response;
import javatechy.codegen.service.ProjectService;

@Entity
//@RequestMapping("${requestMapping}")
public class ${name} {

private Logger logger = Logger.getLogger(${name}.class);

        /**
         @ApiOperation(value = "${docsComment}")
         @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
         public Response createProject(@RequestBody Request request) throws IOException {
         logger.info("Recieved Request => " + request);
         projectService.createProject(request);
         return null;
         }
         **/

        }
