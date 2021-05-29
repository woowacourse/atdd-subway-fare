package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineDetailResponse {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;
    private List<SectionResponse> sections;

    public LineDetailResponse() {
    }

    public LineDetailResponse(Long id, String name, String color, int distance,
                              List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineDetailResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        List<SectionResponse> sections = line.sections().stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return new LineDetailResponse(line.getId(), line.getName(), line.getColor(), line.distance(), stations, sections);
    }

    public static List<LineDetailResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LineDetailResponse::of)
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

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
