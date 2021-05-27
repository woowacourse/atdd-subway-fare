package wooteco.common.exception.badrequest;

public class StationNameDuplicateException extends BadRequestException{

    private static final String MESSAGE = "이미 존재하는 역 이름입니다.";
    public StationNameDuplicateException() {
        super(MESSAGE);
    }
}
