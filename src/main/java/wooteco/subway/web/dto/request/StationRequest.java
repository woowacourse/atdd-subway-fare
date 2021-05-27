package wooteco.subway.web.dto.request;

import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.validator.SubwayName;

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
