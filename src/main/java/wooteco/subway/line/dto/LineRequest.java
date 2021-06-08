package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    @Positive
    private Long upStationId;
    @NotNull
    @Positive
    private Long downStationId;
    @PositiveOrZero
    private int extraFare;
    @Positive
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int extraFare, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.extraFare = extraFare;
        this.distance = distance;
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

    public int getExtraFare() {
        return extraFare;
    }

    public int getDistance() {
        return distance;
    }
}
