package wooteco.common.exception.badrequest;

public class NotRemovalStationException extends BadRequestException {

    private static final String MESSAGE = "구간에 등록되어있어 삭제할 수 없는 역입니다.";

    public NotRemovalStationException() {
        super(MESSAGE);
    }
}
