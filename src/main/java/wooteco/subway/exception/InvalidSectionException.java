package wooteco.subway.exception;

public class InvalidSectionException extends InvalidException {

    public InvalidSectionException() {
        super("존재하지 않는 구간입니다.");
    }
}
