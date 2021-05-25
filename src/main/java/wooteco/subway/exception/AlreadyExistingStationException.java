package wooteco.subway.exception;

public class AlreadyExistingStationException extends SubwayException {

    public AlreadyExistingStationException() {
        super("이미 존재하는 역 이름으로 수정할 수 없습니다.");
    }
}
