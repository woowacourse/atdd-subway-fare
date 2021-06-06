package wooteco.subway.line.dto;

import wooteco.subway.line.exception.InvalidLineColorException;
import wooteco.subway.line.exception.InvalidLineNameException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {
    @NotBlank(message = InvalidLineNameException.ERROR_MESSAGE)
    private String name;

    @NotBlank(message = InvalidLineColorException.ERROR_MESSAGE)
    private String color;

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @PositiveOrZero(message = InvalidLineNameException.ERROR_MESSAGE)
    private int distance;

    @PositiveOrZero(message = InvalidLineNameException.ERROR_MESSAGE)
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
