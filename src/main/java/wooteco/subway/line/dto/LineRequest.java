package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[가-힣]*$")
    private String name;
    @NotNull
    private String color;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @Min(0)
    private int fare;
    @Min(1)
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance) {
        this(name, color, upStationId, downStationId, 0, distance);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int fare,
        int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.fare = fare;
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

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }
}
