package wooteco.subway.station.dto;

import javax.validation.constraints.NotBlank;
import wooteco.subway.station.domain.Station;

public class StationRequest {
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
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
