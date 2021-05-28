package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class LineWithTransferLinesResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;

    public LineWithTransferLinesResponse() {
    }

    public LineWithTransferLinesResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineWithTransferLinesResponse(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }
}
