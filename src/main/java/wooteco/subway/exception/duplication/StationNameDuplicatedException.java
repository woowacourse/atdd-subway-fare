package wooteco.subway.exception.duplication;

public class StationNameDuplicatedException extends DuplicatedException {
    private static final String MESSAGE = "존재하는 역 이름입니다.";

    public StationNameDuplicatedException() {
        super(MESSAGE);
    }
}
