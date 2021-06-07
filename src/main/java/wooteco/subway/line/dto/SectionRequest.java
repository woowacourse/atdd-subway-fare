package wooteco.subway.line.dto;

import wooteco.subway.line.NotEqual;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NotEqual(upStationId = "upStationId", downStationId = "downStationId")
public class SectionRequest {
    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @NotNull
    @Positive(message = "거리는 양수여야 합니다.")
    private Integer distance;

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
