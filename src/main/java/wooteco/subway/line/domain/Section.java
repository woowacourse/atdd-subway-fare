package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Station upStation, Station downStation, int distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
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
        return distance.toInt();
    }
}
