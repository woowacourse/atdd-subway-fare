package wooteco.common.exception.badrequest;

public class LineColorDuplicateException extends BadRequestException {

    private static final String MESSAGE = "이미 존재하는 노선 색이 있습니다.";

    public LineColorDuplicateException() {
        super(MESSAGE);
    }
}
