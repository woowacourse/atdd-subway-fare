package wooteco.subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class LineRequest {
    @NotBlank(message = "INVALID_NAME")
    @Pattern(regexp = "^[가-힣0-9]*$", message = "INVALID_NAME")
    private String name;
    @NotBlank(message = "INVALID_NAME")
    private String color;
    private Long upStationId;
    private Long downStationId;
    @Positive(message = "INVALID_DISTANCE")
    private int distance;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
