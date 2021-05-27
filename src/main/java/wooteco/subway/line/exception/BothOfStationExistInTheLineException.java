package wooteco.subway.line.exception;

public class BothOfStationExistInTheLineException extends LineDomainException {
    public static final String MESSAGE = "등록하려는 구간의 상행, 하행 역이 모두 노선에 이미 등록되어 있습니다.";

    public BothOfStationExistInTheLineException() {
        super(MESSAGE);
    }
}
