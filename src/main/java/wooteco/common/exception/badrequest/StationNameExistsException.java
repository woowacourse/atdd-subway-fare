package wooteco.common.exception.badrequest;

public class StationNameExistsException extends BadRequestException{

    private static final String MESSAGE = "이미 존재하는 역 이름입니다.";
    public StationNameExistsException() {
        super(MESSAGE);
    }
}
