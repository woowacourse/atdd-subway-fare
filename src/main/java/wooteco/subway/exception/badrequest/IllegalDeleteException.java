package wooteco.subway.exception.badrequest;

public class IllegalDeleteException extends BadRequest {

    public IllegalDeleteException(String message) {
        super(message);
    }

    public IllegalDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
