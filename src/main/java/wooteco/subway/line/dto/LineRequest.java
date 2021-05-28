package wooteco.subway.line.dto;

import wooteco.subway.exception.InvalidInsertException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {
    public static final int DEFAULT_EXTRAFARE = 0;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String color;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long upStationId;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long downStationId;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요") @Positive(message = "거리는 1 이상의 숫자를 입력해주세요")
    private int distance;
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, DEFAULT_EXTRAFARE);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        validateExtraFare(extraFare);
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < DEFAULT_EXTRAFARE) {
            throw new InvalidInsertException("추가요금은 음수일 수 없습니다.");
        }
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
