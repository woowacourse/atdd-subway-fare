package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long upStationId;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
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
