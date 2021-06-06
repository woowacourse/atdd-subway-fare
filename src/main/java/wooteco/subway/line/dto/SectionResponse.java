package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;

public class SectionResponse {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance());
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
