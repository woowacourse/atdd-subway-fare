package wooteco.subway.exception;

public class SubwayException extends RuntimeException {
    private final String message;

    public SubwayException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
