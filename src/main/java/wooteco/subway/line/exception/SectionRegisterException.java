package wooteco.subway.line.exception;

public class SectionRegisterException extends RuntimeException {
    public static final String ERROR_MESSAGE = "구간으로 등록될 수 없습니다.";

    public SectionRegisterException() {
        super(ERROR_MESSAGE);
    }
}
