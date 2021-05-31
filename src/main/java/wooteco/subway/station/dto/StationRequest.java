package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다")
    @Size(min = 2, max = 20, message = "역 이름은 2자리 에서 20자리 사이여야 합니다.")
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
