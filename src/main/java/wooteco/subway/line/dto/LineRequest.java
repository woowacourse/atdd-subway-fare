package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class LineRequest {
    @NotEmpty(message = "INVALID_NAME")
    @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$", message = "INVALID_NAME")
    private String name;

    @NotEmpty(message = "INVALID_INPUT")
    @NotBlank(message = "INVALID_INPUT")
    private String color;

    @NotNull(message = "INVALID_INPUT")
    private Long upStationId;

    @NotNull(message = "INVALID_INPUT")
    private Long downStationId;

    @NotNull(message = "INVALID_INPUT")
    @Positive(message = "INVALID_DISTANCE")
    private int distance;

    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this(name, color, upStationId, downStationId, distance);
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
