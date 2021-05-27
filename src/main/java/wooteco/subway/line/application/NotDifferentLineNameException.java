package wooteco.subway.line.application;

public class NotDifferentLineNameException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선 이름 입니다.";

    public NotDifferentLineNameException() {
        super(ERROR_MESSAGE);
    }
}
