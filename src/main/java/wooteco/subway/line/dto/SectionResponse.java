package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {
    StationResponse upStation;
    StationResponse downStation;
    int distance;

    private SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(StationResponse.of(section.getUpStation()),
                StationResponse.of(section.getUpStation()), section.getDistance());
    }
}
