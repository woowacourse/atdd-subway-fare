package wooteco.subway.exception.badrequest;

public class InvalidStationsOfSectionException extends BadRequestException {
    public InvalidStationsOfSectionException() {
        super("구간에 대한 지하철 역 입력이 잘못되었습니다.");
    }
}
