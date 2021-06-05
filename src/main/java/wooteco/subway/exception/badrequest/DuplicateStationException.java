package wooteco.subway.exception.badrequest;

public class DuplicateStationException extends BadRequestException {
    public DuplicateStationException() {
        super("이미 존재하는 지하철 역입니다.");
    }
}
