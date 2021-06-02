package wooteco.subway.exception;

public class DuplicatedFieldException extends RuntimeException {

    public DuplicatedFieldException(String description) {
        super(String.format("값이 중복되었습니다.(%s)", description));
    }
}
