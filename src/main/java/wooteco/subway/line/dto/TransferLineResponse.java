package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class TransferLineResponse {
    private Long id;
    private String name;
    private String color;

    public TransferLineResponse() {
    }

    public TransferLineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static List<TransferLineResponse> listOf(final List<Line> lines) {
        return lines.stream()
                .map(it -> new TransferLineResponse(it.getId(), it.getName(), it.getColor()))
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
