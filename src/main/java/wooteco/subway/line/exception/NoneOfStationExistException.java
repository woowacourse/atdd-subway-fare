package wooteco.subway.line.exception;

public class NoneOfStationExistException extends LineDomainException {
    public static final String MESSAGE = "추가하려는 구간의 상행, 하행역이 기존 노선에 존재하지 않습니다.";

    public NoneOfStationExistException() {
        super(MESSAGE);
    }
}
