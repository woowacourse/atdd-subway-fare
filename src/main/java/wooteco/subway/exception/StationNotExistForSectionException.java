package wooteco.subway.exception;

public class StationNotExistForSectionException extends CannotAddSectionException {

    private static final String STATION_NOT_EXIST_FOR_SECTION = "노선에 등록되지 않은 역으로 구간을 생성할 수 없습니다.";

    public StationNotExistForSectionException() {
        super(STATION_NOT_EXIST_FOR_SECTION);
    }
}
