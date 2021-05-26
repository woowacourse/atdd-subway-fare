package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String name;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String color;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long upStationId;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    private Long downStationId;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요") @PositiveOrZero(message = "거리는 0 이상의 숫자를 입력해주세요")
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
}
