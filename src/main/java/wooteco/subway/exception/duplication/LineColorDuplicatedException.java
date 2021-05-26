package wooteco.subway.exception.duplication;

public class LineColorDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "잘못된 노선 색상입니다.";

    public LineColorDuplicatedException() {
        super(MESSAGE);
    }
}
