package wooteco.subway.line.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {
    @NotNull(message = "INVALID_INPUT")
    private Long upStationId;

    @NotNull(message = "INVALID_INPUT")
    private Long downStationId;

    @NotNull(message = "INVALID_INPUT")
    @Positive(message = "INVALID_DISTANCE")
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Integer distance) {
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
