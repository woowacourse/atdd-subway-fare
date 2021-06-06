package wooteco.common.exception.badrequest;

public class NotRemovalStationException extends BadRequestException {
    public NotRemovalStationException(Long id) {
        super(id + "역은 구간에 포함되어 있어 삭제 할 수 없습니다.");
    }
}
