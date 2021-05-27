package wooteco.subway.member.application;

public class EmailNotFoundException extends IllegalArgumentException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
