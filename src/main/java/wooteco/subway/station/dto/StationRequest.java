package wooteco.subway.station.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import wooteco.subway.station.domain.Station;

public class StationRequest {
    @NotEmpty(message = "INVALID_NAME")
    @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$", message = "INVALID_NAME")
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
