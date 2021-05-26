package wooteco.subway.exception;

public class DuplicatedLineException extends SubwayException {
    private static final String ERROR = "DUPLICATED_LINE_NAME";
    private static final String MESSAGE = "중복되는 노선이 존재합니다.";

    public DuplicatedLineException() {
        super(ERROR, MESSAGE);
    }
}
