package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @NotBlank(message = "이름에 공백이 있을 수 없습니다.")
    @Length(min = 2, max = 10, message = "역 이름은 2글자 이상 10글자 이하여야합니다.")
    @Pattern(regexp = "^[가-힣|0-9]*$")
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int fare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int fare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.fare = fare;
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

    public int getFare() {
        return fare;
    }
}
