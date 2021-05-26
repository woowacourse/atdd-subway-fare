package wooteco.subway.station.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import wooteco.subway.station.domain.Station;

public class StationRequest {
    @NotEmpty(message = "역 이름은 공백이 아닙니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$", message = "역 이름에 특수문자가 들어올 수 없습니다.")
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
