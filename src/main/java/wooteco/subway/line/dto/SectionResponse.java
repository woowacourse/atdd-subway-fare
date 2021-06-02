package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<SectionResponse> listOf(Sections sections) {
        return sections.getSections().stream()
                .map(t -> new SectionResponse(StationResponse.of(t.getUpStation()), StationResponse.of(t.getDownStation()), t.getDistance()))
                .collect(Collectors.toList());
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
