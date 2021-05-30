package wooteco.subway.line.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {
    @NotNull(message = "상행 역은 공백일 수 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행 역은 공백일 수 없습니다.")
    private Long downStationId;

    @Positive(message = "구간 거리는 0보다 길어야 합니다.")
    @NotNull(message = "구간 거리는 공백일 수 없습니다.")
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
