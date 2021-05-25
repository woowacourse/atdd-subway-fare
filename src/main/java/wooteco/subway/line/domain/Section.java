package wooteco.subway.line.domain;

import wooteco.subway.path.domain.Distance;
import wooteco.subway.station.domain.Station;

public class Section {
    private Long id;
    private Station upStation;
    private Station downStation;
    private Distance distance;

    public Section() {
    }

    public Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceASInt() {
        return distance.getDistance();
    }

    public boolean hasUpStation(final Station station) {
        return this.upStation.equals(station);
    }

    public boolean contains(final Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public boolean isShorterOrEqualTo(final Section that) {
        return this.distance.isShorterOrEqualTo(that.distance);
    }

    public Distance getRemainingDistance(final Section that) {
        return this.distance.subtract(that.distance);
    }
}
