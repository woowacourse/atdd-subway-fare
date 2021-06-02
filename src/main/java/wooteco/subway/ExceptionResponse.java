package wooteco.subway;

public class ExceptionResponse {

    private static final String regex = "([a-z])([A-Z]+)";
    private static final String replacement = "$1_$2";

    private String error;
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Exception exception) {
        this.error = exception.getClass().getSimpleName().
            replace("Exception", "")
            .replaceAll(regex, replacement)
            .toUpperCase();
        this.message = exception.getMessage();
    }

    public ExceptionResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
