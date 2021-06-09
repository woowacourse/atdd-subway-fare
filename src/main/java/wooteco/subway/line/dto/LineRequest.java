package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {

    @NotBlank(message = "이름이 비어있거나 공백입니다.")
    private String name;

    @NotBlank(message = "색상이 비어있거나 공백입니다.")
    private String color;

    @NotNull(message = "상행역이 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행역이 없습니다.")
    private Long downStationId;

    @Positive(message = "거리는 양수여야 합니다.")
    private int distance;

    @PositiveOrZero(message = "추가요금은 0이상이어야 합니다.")
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
