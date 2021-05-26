package wooteco.subway.station.dto;

import javax.validation.constraints.Pattern;
import wooteco.subway.station.domain.Station;

public class StationRequest {

    @Pattern(regexp = "[0-9a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ]*")
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
