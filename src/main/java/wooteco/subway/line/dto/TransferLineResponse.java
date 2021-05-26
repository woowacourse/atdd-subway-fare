package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;


public class TransferLineResponse {

    private Long id;
    private String name;
    private String color;

    public TransferLineResponse() {
    }

    public TransferLineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static TransferLineResponse of(Line line) {
        return new TransferLineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static TransferLineResponse of(Long id, String name, String color) {
        return new TransferLineResponse(id, name, color);
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




