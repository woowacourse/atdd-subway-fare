package wooteco.subway.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
