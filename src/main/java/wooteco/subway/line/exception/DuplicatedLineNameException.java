package wooteco.subway.line.exception;

public class DuplicatedLineNameException extends RuntimeException {

    public DuplicatedLineNameException(String message) {
        super(message);
    }
}
