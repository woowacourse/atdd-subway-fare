package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionInLineResponse {
    private StationResponse upStationResponse;
    private StationResponse downStationResponse;
    private int distance;

    public SectionInLineResponse(StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }

    public static SectionInLineResponse of(Section section) {
        StationResponse up = StationResponse.of(section.getUpStation());
        StationResponse down = StationResponse.of(section.getDownStation());
        int distance = section.getDistance();
        return new SectionInLineResponse(up, down, distance);
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
