package wooteco.subway.web.dto.response;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

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
