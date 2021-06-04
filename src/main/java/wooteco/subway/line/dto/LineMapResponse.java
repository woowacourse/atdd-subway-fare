package wooteco.subway.line.dto;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.dto.SectionResponse;

public class LineMapResponse {

    private Long id;
    private String name;
    private String color;
    private int distance;
    private int extraFare;
    private List<SectionResponse> sections;

    public LineMapResponse() {
    }

    public LineMapResponse(Long id, String name, String color, int distance, int extraFare, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public static LineMapResponse of(Line line, List<SectionResponse> sectionResponses) {
        int totalDistance = sectionResponses.stream()
            .mapToInt(SectionResponse::getDistanceToNextStation)
            .sum();

        return new LineMapResponse(line.getId(), line.getName(), line.getColor(), totalDistance, line.getExtraFare(), sectionResponses);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
