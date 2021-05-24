package wooteco.subway.station.ui.dto;

import wooteco.subway.station.domain.Station;

import java.beans.ConstructorProperties;

public class StationRequest {

    private final String name;

    @ConstructorProperties("name")
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
