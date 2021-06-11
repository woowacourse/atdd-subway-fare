package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.station.dto.StationResponse;

public class CustomLineResponse {

    private Long id;
    private String name;
    private String color;
    private StationResponse startStation;
    private StationResponse endStation;
    private int distance;

    public CustomLineResponse() {
    }

    public CustomLineResponse(Long id, String name, String color, StationResponse startStation, StationResponse endStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static CustomLineResponse of(Line line, Sections sections) {
        return new CustomLineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            StationResponse.of(sections.startStation()),
            StationResponse.of(sections.endStation()),
            sections.totalDistanceValue()
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
