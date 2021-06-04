package wooteco.subway.line.dto;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.line.domain.Line;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LineRequest {

    @Pattern(regexp = "^[가-힣|0-9]*$", message = "노선명은 숫자와 한글만 가능합니다.")
    @Length(min = 2, max = 10, message = "노선명은 2자 이상만 가능합니다.")
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
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

    public Line toLine() {
        return new Line(name, color, extraFare);
    }
}
