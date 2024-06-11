package javatechy.codegen.exception;

import org.apache.log4j.Logger;

import java.io.Serial;


public class CodeGenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public static Logger logger = Logger.getLogger(CodeGenException.class);

    public CodeGenException(String code) {
        super(code);
        logger.error("Execption occured due to code : " + code, this);
    }

}
