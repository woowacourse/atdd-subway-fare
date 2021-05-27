package wooteco.subway.line.dto;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.line.exception.InvalidLineColorException;
import wooteco.subway.line.exception.InvalidLineNameException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LineRequest {
    @NotBlank(message = InvalidLineNameException.ERROR_MESSAGE)
    @Length(min = 2, message = InvalidLineNameException.ERROR_MESSAGE)
    @Pattern(regexp = "^[가-힣|0-9]*$", message = InvalidLineNameException.ERROR_MESSAGE)
    private String name;

    @NotBlank(message = InvalidLineColorException.ERROR_MESSAGE)
    private String color;

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @NotNull
    private int distance;

    @NotNull
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
