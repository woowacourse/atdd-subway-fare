package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Sections;

import java.util.List;
import java.util.stream.Collectors;

public class LinesResponse {
    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sections;

    public LinesResponse() {
    }

    public LinesResponse(Long id, String name, String color, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public LinesResponse(Long id, String name, String color, Sections sections) {
        this(
                id,
                name,
                color,
                sections.getSections().stream()
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

    public List<SectionResponse> getSections() {
        return sections;
    }
}
