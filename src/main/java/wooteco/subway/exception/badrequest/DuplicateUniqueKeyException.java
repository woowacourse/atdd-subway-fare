package wooteco.subway.exception.badrequest;

public class DuplicateUniqueKeyException extends BadRequest {
    public DuplicateUniqueKeyException(String message) {
        super(message);
    }

    public DuplicateUniqueKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
