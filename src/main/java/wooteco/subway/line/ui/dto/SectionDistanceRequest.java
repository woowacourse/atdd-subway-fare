package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;

public class SectionDistanceRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    @ConstructorProperties({"upStationId", "downStationId", "distance"})
    public SectionDistanceRequest(Long upStationId, Long downStationId, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }

}
