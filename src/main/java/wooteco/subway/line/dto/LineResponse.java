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
    private int extraFare;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.extraFare = extraFare;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getExtraFare());
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

    public int getExtraFare() {
        return extraFare;
    }
}
