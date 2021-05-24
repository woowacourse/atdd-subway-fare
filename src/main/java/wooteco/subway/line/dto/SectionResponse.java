package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static List<SectionResponse> listOf(List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionResponse(StationResponse.of(section.getUpStation()),
                        StationResponse.of(section.getDownStation()), section.getDistance()))
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
