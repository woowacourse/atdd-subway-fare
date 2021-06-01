package wooteco.subway.exception;

public class DuplicateLineException extends DuplicateException {

    public DuplicateLineException() {
        super("중복된 노선 이름입니다.");
    }
}
