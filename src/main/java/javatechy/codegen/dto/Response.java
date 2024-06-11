package javatechy.codegen.dto;

public class Response {

    private String status;
    private String code;
    private String error;
    private String message;

    public Response(String status) {
        this.status = status;
        // TODO Auto-generated constructor stub
    }
    public Response(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
