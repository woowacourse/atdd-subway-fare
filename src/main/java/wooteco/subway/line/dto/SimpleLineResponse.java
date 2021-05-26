package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;

public class SimpleLineResponse {

    private final Long id;
    private final String name;
    private final String color;

    public SimpleLineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static List<SimpleLineResponse> listOf(List<Line> simpleLines) {
        return simpleLines.stream()
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
