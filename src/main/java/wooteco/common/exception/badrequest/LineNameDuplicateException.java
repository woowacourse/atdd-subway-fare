package wooteco.common.exception.badrequest;

public class LineNameDuplicateException extends BadRequestException {

    private static final String MESSAGE = "이미 존재하는 노선 이름이 있습니다.";

    public LineNameDuplicateException() {
        super(MESSAGE);
    }
}
