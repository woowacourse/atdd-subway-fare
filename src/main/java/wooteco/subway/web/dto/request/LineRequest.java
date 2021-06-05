package wooteco.subway.web.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.web.dto.validator.SubwayName;

public class LineRequest {

    @SubwayName
    private String name;
    @NotEmpty
    private String color;
    @NotNull
    @Positive
    private Long upStationId;
    @NotNull
    @Positive
    private Long downStationId;
    @Positive
    @NotNull
    private Integer distance;
    @PositiveOrZero
    @NotNull
    private Integer extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        Integer distance, Integer extraFare) {
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
