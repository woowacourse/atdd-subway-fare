package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
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

    public static SectionResponse from(Section section) {
        return new SectionResponse(StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getDownStation()),
                section.getDistance());
    }

    public static List<SectionResponse> listOf(List<Section> sectionsAsList) {
        return sectionsAsList.stream()
                .map(SectionResponse::from)
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
