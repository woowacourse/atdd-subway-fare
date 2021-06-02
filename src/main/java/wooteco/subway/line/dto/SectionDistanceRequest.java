package wooteco.subway.line.dto;

import javax.validation.constraints.Min;

public class SectionDistanceRequest {

    @Min(1)
    private int distance;

    public SectionDistanceRequest() {
    }

    public SectionDistanceRequest(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
