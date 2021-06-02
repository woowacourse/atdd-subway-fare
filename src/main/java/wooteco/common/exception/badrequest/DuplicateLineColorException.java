package wooteco.common.exception.badrequest;

public class DuplicateLineColorException extends BadRequestException {
    private static final String DUPLICATE_COLOR_ERROR_MESSAGE = "이미 존재하는 노선 색상입니다.";

    public DuplicateLineColorException() {
        super(DUPLICATE_COLOR_ERROR_MESSAGE);
    }
}
