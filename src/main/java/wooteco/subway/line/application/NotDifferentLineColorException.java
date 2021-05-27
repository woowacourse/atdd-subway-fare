package wooteco.subway.line.application;

public class NotDifferentLineColorException extends RuntimeException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 색깔 입니다.";

    public NotDifferentLineColorException() {
        super(ERROR_MESSAGE);
    }
}
