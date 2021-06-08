package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {

    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(
        Long id,
        StationResponse upStation,
        StationResponse downStation,
        int distance
    ) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
            section.getId(),
            StationResponse.of(section.getUpStation()),
            StationResponse.of(section.getDownStation()),
            section.getDistance()
        );
    }

    public Long getId() {
        return id;
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
