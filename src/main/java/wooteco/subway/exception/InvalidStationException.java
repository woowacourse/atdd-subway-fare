package wooteco.subway.exception;

public class InvalidStationException extends InvalidException {

    public InvalidStationException() {
        super("존재하지 않는 역입니다.");
    }
}
