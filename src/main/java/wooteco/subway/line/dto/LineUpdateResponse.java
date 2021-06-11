package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineUpdateResponse {
    private final Long id;
    private final String name;
    private final String color;

    public LineUpdateResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineUpdateResponse of(Line line) {
        return new LineUpdateResponse(line.getId(), line.getName(), line.getColor());
    }

    public static List<LineUpdateResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LineUpdateResponse::of)
                .collect(toList());
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
}
