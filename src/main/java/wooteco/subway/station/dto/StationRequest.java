package wooteco.subway.station.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class StationRequest {
    @NotBlank(message = "INVALID_NAME")
    @Pattern(regexp = "^[가-힣0-9]$", message = "INVALID_NAME")
    private String name;

    public StationRequest(String name) {
        this.name = name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
