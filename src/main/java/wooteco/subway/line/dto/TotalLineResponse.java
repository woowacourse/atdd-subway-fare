package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

public class TotalLineResponse {
    private Long id;
    private String name;
    private String color;
    private Station startStation;
    private Station endStation;
    private int distance;

    public TotalLineResponse(Long id, String name, String color,
                             Station startStation, Station endStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static TotalLineResponse of(Line line) {
        return new TotalLineResponse(
                line.getId(), line.getName(), line.getColor(),
                line.startStation(), line.endStation(), line.totalDistance());
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

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
