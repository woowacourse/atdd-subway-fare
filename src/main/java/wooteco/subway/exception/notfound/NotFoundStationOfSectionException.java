package wooteco.subway.exception.notfound;

public class NotFoundStationOfSectionException extends NotFoundException {
    public NotFoundStationOfSectionException() {
        super("상행 종점역이 존재하지 않습니다.");
    }
}
