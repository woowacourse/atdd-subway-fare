package wooteco.common.exception.badrequest;

public class InvalidPathException extends BadRequestException {

    private static final String MESSAGE = "잘못된 구간 정보입니다.";

    public InvalidPathException() {
        super(MESSAGE);
    }
}
