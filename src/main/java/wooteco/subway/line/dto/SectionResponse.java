package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;

public class SectionResponse {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionResponse(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        Long upStationId = section.getUpStation().getId();
        Long downStationId = section.getDownStation().getId();
        return new SectionResponse(upStationId, downStationId, section.getDistance());
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
