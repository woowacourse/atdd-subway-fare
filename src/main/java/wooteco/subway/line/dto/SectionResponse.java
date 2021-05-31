package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    public SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        final StationResponse upStation = StationResponse.of(section.getUpStation());
        final StationResponse downStation = StationResponse.of(section.getDownStation());
        return new SectionResponse(upStation, downStation, section.getDistance());
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
