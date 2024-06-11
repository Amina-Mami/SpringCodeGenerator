package ${groupId}.${artifactId}.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javatechy.codegen.common.Constant;
import javatechy.codegen.config.Config;
import javatechy.codegen.dto.Response;

/**
 * Global Exception Handler
 * 
 * @author ${developerName}
 *
 */
@RestControllerAdvice
public class ExceptionInterceptor {
    public static Logger logger = Logger.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(value = CodeGenException.class)
    public Response authenticationFailure(HttpServletRequest request, HttpServletResponse response, CodeGenException ex) {
        logger.info("Exception Occured with code => " + ex.getMessage());
        return buildErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Response parentException(HttpServletRequest httpRequest, HttpServletResponse response, Exception ex) {
        logger.error("Unknown Exception found", ex);
        return buildErrorResponse(Constant.EX_UNKOWN_EXCEPTION_CODE);
    }
}
