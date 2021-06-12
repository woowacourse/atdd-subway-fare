package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름은 빈칸일 수 없습니다.")
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
