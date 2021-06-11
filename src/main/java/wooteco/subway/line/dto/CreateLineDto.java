package wooteco.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.line.domain.Line;

public class CreateLineDto {

    @NotEmpty
    private final String name;
    @NotBlank
    private final String color;
    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @Min(1)
    private final int distance;
    @PositiveOrZero
    private final int extraFare;

    public CreateLineDto(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static CreateLineDto from(LineRequest dto) {
        return new CreateLineDto(dto.getName(), dto.getColor(), dto.getUpStationId(),
            dto.getDownStationId(), dto.getDistance(), dto.getExtraFare());
    }

    public Line toLineEntity() {
        return new Line(name, color, extraFare);
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
