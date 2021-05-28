package wooteco.subway.line.dto;

import java.util.Comparator;

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
