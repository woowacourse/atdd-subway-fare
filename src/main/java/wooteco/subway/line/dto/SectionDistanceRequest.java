package wooteco.subway.line.dto;

public class SectionDistanceRequest {
    private Integer distance;

    public SectionDistanceRequest() {
    }

    public SectionDistanceRequest(final Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }
}
