package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class MapResponse {
    private Long id;
    private String name;
    private String color;
    private Long extraFare;
    private List<SectionResponse> sections;

    public MapResponse(Long id, String name, String color, Long extraFare, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public static MapResponse of(Line line) {
        List<SectionResponse> sections = line.sections().stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
        return new MapResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), sections);
    }

    public static List<MapResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(MapResponse::of)
                .collect(Collectors.toList());
    }
}
