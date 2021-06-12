package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class SimpleLineResponse {
    private final Long id;
    private final String name;
    private final String color;

    public SimpleLineResponse(Line line) {
        this(line.getId(), line.getName(), line.getColor());
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
