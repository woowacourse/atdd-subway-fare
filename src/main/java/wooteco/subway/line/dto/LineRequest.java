package wooteco.subway.line.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "이름에 공백이 있을 수 없습니다.")
    @Length(min = 2, max = 20, message = "노선 이름은 2이상 20이하여야 합니다.")
    private String name;
    private String color;

    @NotNull(message = "상행선 입력이 비어있을 수 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행선 입력이 비어있을 수 없습니다.")
    private Long downStationId;

    @Min(value = 0, message = "거리는 음수 일 수 없습니다.")
    private int distance;

    @Min(value = 0, message = "금액은 음수 일 수 없습니다.")
    private int extraFare;

    public LineRequest() {
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
