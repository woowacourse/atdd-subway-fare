package wooteco.subway.station.dto;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;

import javax.validation.constraints.Pattern;

public class StationRequest {

    @Pattern(regexp = "^[가-힣|0-9]*$", message = "지하철 역명은 숫자와 한글만 가능합니다.")
    @Length(min = 2, max = 10, message = "지하철 역명은 2자 이상만 가능합니다.")
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
