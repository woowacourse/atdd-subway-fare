package wooteco.subway.station.exception;

public class ExistedStationOnPathException extends RuntimeException {

    public ExistedStationOnPathException() {
        super("구간 위에 역이 존재를 합니다.");
    }
}
