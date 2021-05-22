package wooteco.subway.exception;

public class SubwayExceptionResponse {
    private int httpStatus;
    private String message;

    public SubwayExceptionResponse() {
    }

    public SubwayExceptionResponse(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
