package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class CannotRemoveStationException extends SubwayException {

    private static final String CANNOT_REMOVE_STATION_MESSAGE = "해당 역이 등록된 구간이 있어 삭제할 수 없습니다.";

    public CannotRemoveStationException() {
        super(HttpStatus.BAD_REQUEST, CANNOT_REMOVE_STATION_MESSAGE);
    }
}
