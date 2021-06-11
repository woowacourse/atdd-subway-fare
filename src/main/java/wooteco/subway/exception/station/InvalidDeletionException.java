package wooteco.subway.exception.station;

import wooteco.subway.exception.SubwayException;

public class InvalidDeletionException extends SubwayException {
    public InvalidDeletionException() {
        super("이미 노선에 등록된 지하철 역입니다");
    }
}
