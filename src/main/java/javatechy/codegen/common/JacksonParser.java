package javatechy.codegen.common;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javatechy.codegen.exception.CodeGenException;

public class JacksonParser {
    private static Logger logger = Logger.getLogger(JacksonParser.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> jacksonObjectToMap(String jsonStr) {
        try {
            return mapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error("Error while converting string to json" + jsonStr, e);
            throw new CodeGenException(Constant.EX_UNKOWN_EXCEPTION_CODE);
        }
    }

    public static <T> Map<String, String> jacksonObjectToMap(T object) {
        Map<String, String> map = null;
        try {
            map = mapper.readValue(mapper.writeValueAsString(object), new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error("Error while converting objetc to json", e);
            throw new CodeGenException(Constant.EX_UNKOWN_EXCEPTION_CODE);
        }
        return map;
    }

}
