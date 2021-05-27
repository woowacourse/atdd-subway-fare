package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class StationRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    @Pattern(regexp = "^[가-힣0-9]{2,20}$", message = "역 이름은 2~20자 이하의 한글/숫자만 가능합니다")
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
