package wooteco.subway.line.exception;

public class NoSuchLineException extends LineException{
    private static final String MESSAGE = "NO_SUCH_LINE";

    public NoSuchLineException() {
        super(MESSAGE);
    }
}
