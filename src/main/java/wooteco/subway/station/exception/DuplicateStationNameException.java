package wooteco.subway.station.exception;

public class DuplicateStationNameException extends StationDaoException {
    public static final String MESSAGE = "이미 존재하는 역 이름입니다.";

    public DuplicateStationNameException() {
        super(MESSAGE);
    }
}
