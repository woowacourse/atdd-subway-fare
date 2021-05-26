package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class SectionRequest {
    @NotBlank
    private Long upStationId;
    @NotBlank
    private Long downStationId;
    @Min(value = 1, message = "거리는 0 이상의 숫자를 입력해주세요")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
