package wooteco.subway.line.exception;

public abstract class LineDaoException extends RuntimeException {
    public LineDaoException(String message) {
        super(message);
    }
}
