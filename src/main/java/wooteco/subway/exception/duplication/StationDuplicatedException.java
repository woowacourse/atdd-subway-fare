package wooteco.subway.exception.duplication;

public class StationDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "잘못된 역 이름입니다.";

    public StationDuplicatedException() {
        super(MESSAGE);
    }
}
