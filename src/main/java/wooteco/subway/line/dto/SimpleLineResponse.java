package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleLineResponse {
    private Long id;
    private String name;
    private String color;

    public static SimpleLineResponse of(Line line) {
        return new SimpleLineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static List<SimpleLineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(SimpleLineResponse::of)
                .collect(Collectors.toList());
    }

    public SimpleLineResponse() {
    }

    public SimpleLineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
