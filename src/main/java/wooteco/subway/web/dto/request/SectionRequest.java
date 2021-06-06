package wooteco.subway.web.dto.request;

import wooteco.subway.web.dto.validator.SubwayId;

import javax.validation.constraints.Positive;

public class SectionRequest {
    @SubwayId
    private Long upStationId;
    @SubwayId
    private Long downStationId;
    @Positive
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
