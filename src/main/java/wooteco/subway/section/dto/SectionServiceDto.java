package wooteco.subway.section.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.section.domain.Section;

public class SectionServiceDto {

    private final Long id;
    @NotNull
    private final Long lineId;
    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @Min(1)
    private final int distance;

    public SectionServiceDto(Long lineId, Long upStationId, Long downStationId, int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public SectionServiceDto(Long id, Long lineId, Long upStationId, Long downStationId,
        int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionServiceDto from(Section section) {
        return new SectionServiceDto(
            section.getId(),
            section.getLineId(),
            section.getUpStationId(),
            section.getDownStationId(),
            section.getDistanceValue()
        );
    }

    public static SectionServiceDto of(Line line, LineRequest dto) {
        return new SectionServiceDto(line.getId(), dto.getUpStationId(), dto.getDownStationId(),
            dto.getDistance());
    }

    public static SectionServiceDto from(Long lineId, SectionRequest dto) {
        return new SectionServiceDto(lineId, dto.getUpStationId(), dto.getDownStationId(),
            dto.getDistance());
    }

    public Long getId() {
        return id;
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
