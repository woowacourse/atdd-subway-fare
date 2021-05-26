package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class SimpleLineResponse {
    private Long id;
    private String name;
    private String color;

    public SimpleLineResponse() {
    }

    public SimpleLineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static SimpleLineResponse of(Line line) {
        return new SimpleLineResponse(line.getId(), line.getName(), line.getColor());
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
