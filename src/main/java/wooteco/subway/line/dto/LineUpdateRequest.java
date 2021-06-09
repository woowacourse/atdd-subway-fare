package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class LineUpdateRequest {

    @NotBlank(message = "이름이 비어있거나 공백입니다.")
    private String name;

    @NotBlank(message = "색상이 비어있거나 공백입니다.")
    private String color;

    @PositiveOrZero(message = "추가요금은 0이상이어야 합니다.")
    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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
