package wooteco.subway.exception.duplication;

public class LineNameDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "존재하는 노선 이름입니다.";

    public LineNameDuplicatedException() {
        super(MESSAGE);
    }
}
