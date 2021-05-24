package wooteco.subway.line.dto;

public class SectionDistanceRequest {
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
