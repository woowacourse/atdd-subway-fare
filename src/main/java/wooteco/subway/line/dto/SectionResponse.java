package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {

    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public static SectionResponse from(Section section) {
        return new SectionResponse(
            StationResponse.of(section.getUpStation()),
            StationResponse.of(section.getDownStation()),
            section.getDistance()
        );
    }

    public static List<SectionResponse> listOf(Sections sections) {
        return sections.getSections().stream()
            .map(SectionResponse::from)
            .collect(Collectors.toList());
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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