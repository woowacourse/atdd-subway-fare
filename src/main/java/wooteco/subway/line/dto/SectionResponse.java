package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {

    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(Long id, StationResponse upStation,
        StationResponse downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SectionResponse(StationResponse upStation,
        StationResponse downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionResponse of(Section section) {
        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();
        final int distance = section.getDistance();

        return new SectionResponse(
            section.getId(),
            StationResponse.of(upStation),
            StationResponse.of(downStation),
            distance
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
