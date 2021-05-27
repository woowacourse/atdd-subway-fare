package wooteco.subway.member.application;

public class EmailNotFoundException extends IllegalArgumentException {
    private static final String errorMessage = "존재하지 않는 계정입니다";

    public EmailNotFoundException() {
        super(errorMessage);
    }
}
