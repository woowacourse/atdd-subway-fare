package wooteco.subway.member.application;

public class DuplicateEmailException extends IllegalArgumentException {
    private static final String errorMessage = "이미 가입된 이메일 입니다.";

    public DuplicateEmailException() {
        super(errorMessage);
    }
}

