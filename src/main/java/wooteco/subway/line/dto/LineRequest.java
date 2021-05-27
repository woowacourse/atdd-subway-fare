package wooteco.subway.line.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import wooteco.subway.validator.SubwayId;
import wooteco.subway.validator.SubwayName;

public class LineRequest {

    @SubwayName
    private String name;
    @NotEmpty
    private String color;
    @SubwayId
    private Long upStationId;
    @SubwayId
    private Long downStationId;
    @Positive
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
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
