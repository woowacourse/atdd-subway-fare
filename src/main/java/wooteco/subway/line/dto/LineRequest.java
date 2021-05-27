package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class LineRequest {
    @NotEmpty(message = "노선 이름은 공백이 아닙니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-힣0-9]*$", message = "노선 이름에 특수문자가 들어올 수 없습니다.")
    private String name;

    @NotEmpty(message = "노선 색상은 공백이 아닙니다.")
    @NotBlank(message = "노선 색상에 문자열이 없습니다.")
    private String color;

    @NotNull(message = "상행 역은 공백이 아닙니다.")
    private Long upStationId;

    @NotNull(message = "하행 역은 공백이 아닙니다.")
    private Long downStationId;

    @NotNull(message = "구간 거리는 공백이 아닙니다.")
    @Positive(message = "구간 거리는 0보다 커야 합니다.")
    private int distance;

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
}
