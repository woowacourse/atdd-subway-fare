package wooteco.subway.station.exception;

public class DuplicatedStationException extends RuntimeException {

    public DuplicatedStationException(String name) {
        super(String.format("이미 존재하는 역 이름입니다. 이름: %s", name));
    }
}
