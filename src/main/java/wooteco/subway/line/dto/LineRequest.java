package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @NotBlank(message = "이름에 빈 문자열 또는 공백만 있을 수 없습니다.")
    @Length(min = 2, max = 10, message = "노선 이름은 2글자 이상 10글자 이하여야합니다.")
    @Pattern(regexp = "^[가-힣|0-9]*$", message = "노선 이름은 한글 또는 숫자여야 합니다.")
    private String name;

    @NotBlank(message = "색깔에 빈 문자열 또는 공백만 있을 수 없습니다.")
    private String color;

    @NotNull
    @Positive()
    private Long upStationId;

    @NotNull
    @Positive
    private Long downStationId;

    @NotNull
    @Positive(message = "거리는 양수여야 합니다.")
    private int distance;

    @NotNull
    @PositiveOrZero(message = "요금은 0을 포함한 양수여야 합니다.")
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
