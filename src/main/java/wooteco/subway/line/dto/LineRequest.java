package wooteco.subway.line.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.validator.SubwayId;
import wooteco.subway.validator.SubwayName;

public class LineRequest {

    @SubwayName
    private String name;
    @NotEmpty
    private String color;
    @PositiveOrZero
    private int extraFare;
    @SubwayId
    private Long upStationId;
    @SubwayId
    private Long downStationId;
    @Positive
    private int distance;

    public LineRequest() {
    }

    public LineRequest(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        int distance
    ) {
        this.name = name;
        this.color = color;
        this.extraFare = 0;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(
        String name,
        String color,
        int extraFare,
        Long upStationId,
        Long downStationId,
        int distance
    ) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
