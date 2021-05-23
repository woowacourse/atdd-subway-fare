package wooteco.subway.exception.badrequest;

public class DuplicateNameException extends BadRequest {
    public DuplicateNameException(String message) {
        super(message);
    }

    public DuplicateNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
