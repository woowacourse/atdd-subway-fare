package wooteco.subway.member.exception;

public class EmailUpdateTrialException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이메일은 수정할 수 없습니다.";

    public EmailUpdateTrialException() {
        super(ERROR_MESSAGE);
    }
}