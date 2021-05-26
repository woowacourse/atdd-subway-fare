package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class LineDetailResponse {

    private Long id;
    private String name;
    private String color;
    private StationResponse startStation;
    private StationResponse endStation;
    private int distance;

    public LineDetailResponse(Long id, String name, String color,
        StationResponse startStation, StationResponse endStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static LineDetailResponse of(Line line) {
        StationResponse startStation = StationResponse.of(line.getStations().get(0));
        StationResponse endStation = StationResponse
            .of(line.getStations().get(line.getStations().size() - 1));
        return new LineDetailResponse(line.getId(),
            line.getName(),
            line.getColor(),
            startStation,
            endStation,
            line.getSections().getTotalDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public StationResponse getStartStation() {
        return startStation;
    }

    public StationResponse getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
