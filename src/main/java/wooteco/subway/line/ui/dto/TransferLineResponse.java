package wooteco.subway.line.ui.dto;

import wooteco.subway.line.domain.Line;

public class TransferLineResponse {

    private final Long id;
    private final String name;
    private final String color;

    public TransferLineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
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
