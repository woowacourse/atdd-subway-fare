package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class LineWithTransferLinesResponse {
    private final Long id;
    private final String name;
    private final String color;

    public LineWithTransferLinesResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineWithTransferLinesResponse of(Line line) {
        return new LineWithTransferLinesResponse(line.getId(), line.getName(), line.getColor());
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
