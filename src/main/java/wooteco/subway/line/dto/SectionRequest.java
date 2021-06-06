package wooteco.subway.line.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {
    private static final String SECTION_ERROR_MESSAGE = "올바르지 않은 구간 요청 양식입니다.";

    @NotNull(message = SECTION_ERROR_MESSAGE)
    private Long upStationId;

    @NotNull(message = SECTION_ERROR_MESSAGE)
    private Long downStationId;

    @NotNull(message = SECTION_ERROR_MESSAGE)
    @Positive(message = SECTION_ERROR_MESSAGE)
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
