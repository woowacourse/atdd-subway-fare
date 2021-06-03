package wooteco.subway.exception;

public class LineNotFoundException extends NotFoundException {

    private static final String LINE_NOT_FOUND = "해당하는 노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(LINE_NOT_FOUND);
    }
}
