package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;
import wooteco.subway.validator.SubwayName;

public class StationRequest {

    @SubwayName
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
