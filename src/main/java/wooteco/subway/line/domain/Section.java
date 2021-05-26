package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.exception.SectionException;
import wooteco.subway.station.domain.Station;

public class Section {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section() {
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validateDistance(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(0L, upStation, downStation, distance);
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new SubwayCustomException(SectionException.INVALID_SECTION_DISTANCE_EXCEPTION);
        }
    }

    public Long getId() {
        return id;
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
