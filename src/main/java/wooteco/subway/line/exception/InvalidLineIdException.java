package wooteco.subway.line.exception;

public class InvalidLineIdException extends LineDaoException {
    public InvalidLineIdException(String message) {
        super(message);
    }
}
