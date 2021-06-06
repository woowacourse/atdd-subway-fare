package wooteco.subway.exception;

public class DuplicateStationException extends DuplicateException {

    public DuplicateStationException() {
        super("중복된 역 이름입니다.");
    }
}
