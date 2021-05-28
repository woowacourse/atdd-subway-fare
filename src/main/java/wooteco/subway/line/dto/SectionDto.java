package wooteco.subway.line.dto;

import wooteco.subway.station.domain.Station;

public class SectionDto {
    private Station upStation;
    private Station downStation;
    private int distance;

    public SectionDto() {
    }

    public SectionDto(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
