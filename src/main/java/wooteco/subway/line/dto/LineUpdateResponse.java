package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class LineUpdateResponse {
    private Long id;
    private String name;
    private String color;

    public LineUpdateResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineUpdateResponse of(Line line) {
        return new LineUpdateResponse(line.getId(), line.getName(), line.getColor());
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
