package wooteco.subway.station.dto;

import javax.validation.constraints.NotBlank;

public class StationNameRequest {
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;

    public StationNameRequest() {
    }

    public StationNameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
