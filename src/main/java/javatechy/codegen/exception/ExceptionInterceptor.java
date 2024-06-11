package javatechy.codegen.exception;

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
 * @author deepak.kumar
 *
 */
@RestControllerAdvice
public class ExceptionInterceptor {
    public static Logger logger = Logger.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(value = CodeGenException.class)
    public Response authenticationFailure(HttpServletRequest request, HttpServletResponse response, CodeGenException ex) {
        logger.info("HotelierException Occured with code => " + ex.getMessage());
        return buildErrorResponse(ex.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    public Response parentException(HttpServletRequest httpRequest, HttpServletResponse response, Exception ex) {
        logger.error("Unknown Exception found", ex);
        return buildErrorResponse(Constant.EX_UNKOWN_EXCEPTION_CODE);
    }

    /**
     * Build Error Response based on the status and message.
     * 
     * @param status
     *            internal error code based on the Exception
     * @param message
     *            Exception message
     * @return
     */
    private Response buildErrorResponse(String code) {
        Response response = new Response(Constant.FAILURE);
        response.setCode(code);
        String message = Config.errors.get(code);
        response.setError("Some Exception Occured");
        if (message != null)
            response.setError(message);
        return response;
    }

}
