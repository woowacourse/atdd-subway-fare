package wooteco.subway.line.exception.line;

public class DuplicatedLineNameException extends LineException{
    private static final String MESSAGE = "DUPLICATED_LINE_NAME";

    public DuplicatedLineNameException() {
        super(MESSAGE);
    }
}
