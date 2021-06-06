package wooteco.subway.station.dto;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;

public class StationRequest {

    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[가-힣]*$")
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
