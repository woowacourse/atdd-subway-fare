package wooteco.subway.exception.notfound;

public class MemberNotFoundExceptionException extends NotFoundException {
    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundExceptionException() {
        super(MESSAGE);
    }

    public MemberNotFoundExceptionException(String message) {
        super(message);
    }

    public MemberNotFoundExceptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
