package wooteco.subway.station.exception;

import wooteco.subway.exception.WrongNameConventionException;

public class WrongStationNameException extends WrongNameConventionException {

    public WrongStationNameException(String name) {
        super(String.format("해당 역 이름은 잘못된 이름입니다. (%s)", name));
    }

}