package wooteco.subway.station.exception;

public class ExistedStationOnPathException extends RuntimeException {

    public ExistedStationOnPathException() {
        super("노선에 등록된 역은 삭제할 수 없습니다.");
    }
}
