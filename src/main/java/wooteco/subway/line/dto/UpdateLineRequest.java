package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import wooteco.subway.line.domain.Line;

public class UpdateLineRequest {

    @NotEmpty
    private String name;
    @NotBlank
    private String color;
    private int extraFare;

    public UpdateLineRequest() {
    }

    public UpdateLineRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line toLine() {
        return new Line(name, color, extraFare);
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
