package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {

    @NotBlank(message = "잘못된 역 이름입니다.")
    @Size(min = 2, max = 20, message = "잘못된 역 이름입니다.")
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
