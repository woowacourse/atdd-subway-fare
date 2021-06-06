package wooteco.common.exception.badrequest;

public class InvalidPathException extends BadRequestException {

    public InvalidPathException() {
        super("잘못된 경로 조회입니다.");
    }
}
