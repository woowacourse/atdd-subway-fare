package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public final class LinesResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<SectionResponse> sections;

    public LinesResponse() {
    }

    public LinesResponse(Long id, String name, String color, List<SectionResponse> sections) {
        this(id, name, color, 0, sections);
    }

    public LinesResponse(Long id, String name, String color, int extraFare, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public static LinesResponse of(Line line) {
        return new LinesResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getExtraFare(),
                line.getSections().getSections()
                        .stream()
                        .map(SectionResponse::of)
                        .collect(Collectors.toList())
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
