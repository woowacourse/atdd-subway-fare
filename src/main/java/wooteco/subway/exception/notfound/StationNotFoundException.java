package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;

public class StationNotFoundException extends NotFoundException {

    private static final String STATION_NOT_FOUND = "해당하는 역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(HttpStatus.BAD_REQUEST, STATION_NOT_FOUND);
    }
}
