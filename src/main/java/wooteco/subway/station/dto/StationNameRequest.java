package wooteco.subway.station.dto;

import javax.validation.constraints.NotBlank;

public class StationNameRequest {
    @NotBlank
    private String name;

    public StationNameRequest() {
    }

    public StationNameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
