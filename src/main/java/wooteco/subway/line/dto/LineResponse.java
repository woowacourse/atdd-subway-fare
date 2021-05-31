package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long extraFare;
    private List<StationResponse> stations;
    private List<SectionResponse> sections;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Long extraFare, List<StationResponse> stations) {
        this(id, name, color, extraFare, stations, Collections.emptyList());
    }

    public LineResponse(Long id, String name, String color, Long extraFare, List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.stations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stations, SectionResponse.listOf(line.sections()));
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

    public Long getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
