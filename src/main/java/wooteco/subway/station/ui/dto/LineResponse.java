package wooteco.subway.station.ui.dto;

import java.beans.ConstructorProperties;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;

    @ConstructorProperties({"id", "name", "color"})
    public LineResponse(Long id, String name, String color) {
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
