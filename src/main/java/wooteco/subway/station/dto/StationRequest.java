package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class StationRequest {
    @NotBlank(message = "역 이름이 공백입니다.")
    @Pattern(regexp = "^[가-힣|0-9]*$", message = "유효하지 않은 역 이름입니다.")
    @Size(min = 2, message = "두 글자 이상이어야 합니다.")
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
