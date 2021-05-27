package wooteco.subway.member.application;

public class InvalidPasswordException extends RuntimeException {
    private static final String errorMessage = "잘못된 비밀번호 입니다";

    public InvalidPasswordException() {
        super(errorMessage);
    }
}
