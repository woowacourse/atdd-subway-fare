package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class LineRequest {
    @NotBlank(message = "name은 필수로 입력하여야 합니다.")
    @Pattern(regexp = "^[가-힣0-9]{2,10}$", message = "노선 이름은 2~20자 이하의 한글/숫자만 가능합니다")
    private String name;

    @NotBlank(message = "Color은 필수로 입력하여야 합니다.")
    private String color;

    @NotNull(message = "상행역 ID는 필수로 입력하여야 합니다.")
    private Long upStationId;

    @NotNull(message = "하행역 ID는 필수로 입력하여야 합니다.")
    private Long downStationId;

    @NotNull(message = "거리 값은 필수로 입력하여야 합니다.")
    @Positive(message = "거리 값은 0보다 커야 합니다.")
    private Integer distance;

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
