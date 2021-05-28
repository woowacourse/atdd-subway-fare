package wooteco.subway.line.dto;

public class LineNameColorResponse {
    private final Long id;
    private final String name;
    private final String color;

    public LineNameColorResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineNameColorResponse of(LineNameColorDto transferLine) {
        return new LineNameColorResponse(transferLine.getId(), transferLine.getName(), transferLine.getColor());
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
