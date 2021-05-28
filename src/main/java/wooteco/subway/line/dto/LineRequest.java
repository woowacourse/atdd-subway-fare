package wooteco.subway.line.dto;

import javax.validation.constraints.*;

public class LineRequest {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;

    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String color;

    private int extraFare;

    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @PositiveOrZero(message = "지하철역의 ID는 양수여야 합니다")
    private Long upStationId;

    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @PositiveOrZero(message = "지하철역의 ID는 양수여야 합니다")
    private Long downStationId;

    @Positive(message = "거리는 1 이상의 숫자를 입력해주세요")
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

    public LineRequest(String name, String color, Integer extraFare, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public Integer getExtraFare() {
        return extraFare;
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
