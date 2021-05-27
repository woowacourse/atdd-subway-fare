package wooteco.subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class SectionRequest {
    @Positive(message = "INVALID_INPUT")
    private Long upStationId;
    @Positive(message = "INVALID_INPUT")
    private Long downStationId;
    @Positive(message = "INVALID_DISTANCE")
    private int distance;

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
