package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private Integer distance;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static LineResponse of(Line line) {
        int distance = line.getDistance();
        List<StationResponse> stations = line.getStations().stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, distance);
    }

    public static List<LineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LineResponse::of)
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
