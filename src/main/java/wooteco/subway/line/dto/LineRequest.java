package wooteco.subway.line.dto;

import javax.validation.constraints.*;

public class LineRequest {
    @NotBlank(message = "노선 이름이 공백입니다.")
    @Pattern(regexp = "^[가-힣|0-9]*$", message = "유효하지 않은 노선 이름입니다.")
    @Size(min = 2, message = "두 글자 이상이어야 합니다.")
    private String name;

    @NotBlank(message = "유효하지 않은 색상입니다.")
    private String color;

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @NotNull
    @Positive(message = "거리는 양수여야 합니다.")
    private int distance;

    @NotNull
    @PositiveOrZero(message = "추가 요금은 0 이상이어야 합니다.")
    private Integer extraFare;

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
