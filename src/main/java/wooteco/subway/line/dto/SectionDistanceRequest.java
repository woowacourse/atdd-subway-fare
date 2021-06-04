package wooteco.subway.line.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class SectionDistanceRequest {

    @PositiveOrZero
    @NotNull
    private Integer distance;

    public SectionDistanceRequest() {
    }

    public SectionDistanceRequest(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
