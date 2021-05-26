package wooteco.subway.station.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;

public class StationRequest {

    @NotBlank(message = "이름에 공백이 있을 수 없습니다.")
    @Length(min = 2, max = 10, message = "역 이름은 2글자 이상 10글자 이하여야합니다.")
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
