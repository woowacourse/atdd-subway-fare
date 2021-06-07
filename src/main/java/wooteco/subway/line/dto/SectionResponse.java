package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(final Section section) {
        return new SectionResponse(StationResponse.of(section.getUpStation()), StationResponse.of(section.getDownStation()), section.getDistance());
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
