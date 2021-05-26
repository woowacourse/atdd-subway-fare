package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class CustomLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final StationResponse startStation;
    private final StationResponse endStation;
    private final int distance;

    public CustomLineResponse(Long id, String name, String color, StationResponse startStation, StationResponse endStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static CustomLineResponse of(Line line) {
        StationResponse startStation = StationResponse.of(line.getStartStation());
        StationResponse endStation = StationResponse.of(line.getEndStation());
        int distance = line.getTotalDistance();
        return new CustomLineResponse(line.getId(), line.getName(), line.getColor(), startStation, endStation, distance);
    }

    public static List<CustomLineResponse> listOf(List<Line> lines) {
        return lines.stream()
            .map(CustomLineResponse::of)
            .collect(Collectors.toList());
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
