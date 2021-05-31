package wooteco.subway.exception.badrequest;

public class OverDistanceOfSectionException extends BadRequestException {
    public OverDistanceOfSectionException() {
        super("추가할 구간의 길이가 기존 구간의 길이보다 클 수 없습니다.");
    }
}
