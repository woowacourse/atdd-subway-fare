package wooteco.subway.line.application;

public class NoSuchLineException extends RuntimeException {
    private static final String message = "존재하지 않는 노선입니다.";

    public NoSuchLineException() {
        super(message);
    }
}
