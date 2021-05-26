package wooteco.subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class LineUpdateRequest {
    @NotBlank
    @Pattern(regexp = "^[가-힣0-9]{2,10}$")
    private String name;
    @NotBlank
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
