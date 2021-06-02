package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import wooteco.subway.line.domain.Line;

public class UpdateLineRequest {

    @NotEmpty
    private String name;
    @NotBlank
    private String color;

    public UpdateLineRequest() {
    }

    public UpdateLineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line toLine() {
        return new Line(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
