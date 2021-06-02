package wooteco.common.exception.badrequest;

public class DuplicateLineNameException extends BadRequestException {
    private static final String DUPLICATE_NAME_ERROR_MESSAGE = "이미 존재하는 노선 이름입니다.";

    public DuplicateLineNameException() {
        super(DUPLICATE_NAME_ERROR_MESSAGE);
    }
}
