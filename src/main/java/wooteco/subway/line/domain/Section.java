package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.exception.SubwaySectionException;
import wooteco.subway.station.domain.Station;

public class Section {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validate(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(int distance) {
        validateDistance(distance);
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new SubwayCustomException(
                SubwaySectionException.INVALID_SECTION_DISTANCE_EXCEPTION);
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
