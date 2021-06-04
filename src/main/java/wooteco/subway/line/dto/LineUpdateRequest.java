package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;

    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String color;

    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
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
