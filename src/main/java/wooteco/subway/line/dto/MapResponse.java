package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class MapResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;
    private List<SectionResponse> sections;

    public MapResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations,
        List<SectionResponse> sections) {

        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
        this.sections = sections;
    }

    public static MapResponse of(Line line) {
        List<StationResponse> stations = StationResponse.listOf(line.getStations());
        List<SectionResponse> sections = SectionResponse.listOf(line.getSections());
        return new MapResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(),
            stations, sections);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
