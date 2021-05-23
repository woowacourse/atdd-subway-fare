package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;

public class SectionDistanceRequest {

    private final Integer distance;

    @ConstructorProperties("distance")
    public SectionDistanceRequest(Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

}
