package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleLineResponse {

    private Long id;
    private String name;
    private String color;

    public SimpleLineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static List<SimpleLineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(line -> new SimpleLineResponse(line.getId(), line.getName(), line.getColor()))
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
}
