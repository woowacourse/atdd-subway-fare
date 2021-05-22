package wooteco.subway.station.dto;

public class StationNameRequest {
    private String name;

    public StationNameRequest() {
    }

    public StationNameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
