package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    StationResponse upStation;
    StationResponse downStation;
    int distance;

    public SectionResponse() {

    }

    private SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(StationResponse.of(section.getUpStation()),
                StationResponse.of(section.getDownStation()), section.getDistance());
    }

    public static List<SectionResponse> listOf(List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::of)
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
