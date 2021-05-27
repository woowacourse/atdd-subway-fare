package wooteco.subway.line.exception;

public class InvalidLineNameException extends IllegalArgumentException {
    public static final String ERROR_MESSAGE = "2자 이상 한글/숫자로 구성된 노선 이름만 허용합니다.";

    public InvalidLineNameException() {
        super(ERROR_MESSAGE);
    }
}
