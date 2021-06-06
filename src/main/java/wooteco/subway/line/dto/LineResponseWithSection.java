package wooteco.subway.line.dto;

import java.util.List;

import wooteco.subway.line.domain.Line;

public class LineResponseWithSection {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<SectionResponse> sections;

    public LineResponseWithSection() {
    }

    public LineResponseWithSection(Long id, String name, String color, int distance, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
    }

    public LineResponseWithSection(Line line, List<SectionResponse> sectionResponses) {
        this(line.getId(), line.getName(), line.getColor(), line.getTotalDistance(), sectionResponses);
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

    public List<SectionResponse> getSections() {
        return sections;
    }
}
