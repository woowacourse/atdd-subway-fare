package wooteco.subway.station.dto;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "이름에 공백이 있을 수 없습니다.")
    @Length(min = 3, max = 20, message = "역 이름은 3이상 20이하 합니다.")
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
