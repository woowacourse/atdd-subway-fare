package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

public class TransferLinesResponse {
    private Long id;
    private String name;
    private String color;

    public TransferLinesResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static TransferLinesResponse of(Line transferLine) {
        return new TransferLinesResponse(transferLine.getId(), transferLine.getName(), transferLine.getName());
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
