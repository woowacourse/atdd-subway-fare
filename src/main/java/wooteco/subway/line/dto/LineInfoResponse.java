package wooteco.subway.line.dto;

import java.util.List;

import wooteco.subway.station.domain.Station;

public class LineInfoResponse {
    private Long id;
    private String name;
    private String color;
    private Station startStation;
    private Station endStation;
    private int distance;

    public LineInfoResponse() {
    }

    public LineInfoResponse(Long id, String name, String color, Station startStation,
        Station endStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
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