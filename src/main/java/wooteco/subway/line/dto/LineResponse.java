package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                                             .map(it -> StationResponse.of(it))
                                             .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stations);
    }

    public static List<LineResponse> listOf(List<Line> lines) {
        return lines.stream()
                    .map(LineResponse::of)
                    .collect(Collectors.toList());
    }

    public static LineResponse of(Line line, List<Line> persistLines) {
        List<StationResponse> stations = line.getStations().stream()
                                             .map(it -> StationResponse.of(it, persistLines))
                                             .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stations);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
