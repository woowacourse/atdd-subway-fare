package wooteco.subway.station.domain;

public class InvalidStationNameException extends IllegalArgumentException {
    public static final String ERROR_MESSAGE = "2자 이상 한글/숫자로 구성된 역 이름만 허용합니다.";

    public InvalidStationNameException() {
        super(ERROR_MESSAGE);
    }
}
