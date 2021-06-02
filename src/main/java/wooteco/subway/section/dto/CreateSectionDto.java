package wooteco.subway.section.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.SectionRequest;

public class CreateSectionDto {

    @NotNull
    private final Long lineId;
    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @Min(1)
    private final int distance;

    public CreateSectionDto(Long lineId, Long upStationId, Long downStationId, int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static CreateSectionDto of(Long lineId, SectionRequest dto) {
        return new CreateSectionDto(lineId, dto.getUpStationId(), dto.getDownStationId(),
            dto.getDistance());
    }

    public static CreateSectionDto of(Long lineId, LineRequest dto) {
        return new CreateSectionDto(lineId, dto.getUpStationId(), dto.getDownStationId(),
            dto.getDistance());
    }

    public Long getLineId() {
        return lineId;
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
