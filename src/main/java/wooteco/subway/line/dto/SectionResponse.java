package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {

    private StationResponse upStationResponse;
    private StationResponse downStationResponse;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(StationResponse upStationResponse,
        StationResponse downStationResponse, int distance) {
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(StationResponse.of(section.getUpStation()),
            StationResponse.of(section.getDownStation()), section.getDistance());
    }

    public StationResponse getUpStationResponse() {
        return upStationResponse;
    }

    public StationResponse getDownStationResponse() {
        return downStationResponse;
    }

    public int getDistance() {
        return distance;
    }
}
