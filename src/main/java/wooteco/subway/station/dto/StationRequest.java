package wooteco.subway.station.dto;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.InvalidStationNameException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class StationRequest {
    @NotBlank(message = InvalidStationNameException.ERROR_MESSAGE)
    @Length(min = 2, message = InvalidStationNameException.ERROR_MESSAGE)
    @Pattern(regexp = "^[가-힣|0-9]*$", message = InvalidStationNameException.ERROR_MESSAGE)
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
