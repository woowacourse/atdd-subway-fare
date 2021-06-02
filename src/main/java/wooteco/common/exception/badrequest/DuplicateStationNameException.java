package wooteco.common.exception.badrequest;

public class DuplicateStationNameException extends BadRequestException {
    private static final String DUPLICATE_NAME_ERROR_MESSAGE = "이미 존재하는 역 이름입니다.";

    public DuplicateStationNameException() {
        super(DUPLICATE_NAME_ERROR_MESSAGE);
    }
}
