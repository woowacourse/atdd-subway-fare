package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
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
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }

    public static SectionResponse of(Section section) {
        StationResponse upStation = StationResponse.of(section.getUpStation());
        StationResponse downStation = StationResponse.of(section.getDownStation());

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
