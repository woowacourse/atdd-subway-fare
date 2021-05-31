package wooteco.subway.exception.badrequest;

public class ExistStationInSectionException extends BadRequestException {
    public ExistStationInSectionException() {
        super("구간에 존재하는 지하철 역을 삭제할 수 없습니다.");
    }
}
