package wooteco.subway.station.dto;

import wooteco.subway.station.domain.Station;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class StationRequest {
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$", message = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
    @Size(min = 2, max = 20, message = "역의 전체 글자 수는 2자 이상 20자 이하여야 합니다.")
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
