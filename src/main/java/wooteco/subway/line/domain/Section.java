package wooteco.subway.line.domain;

import wooteco.subway.line.exception.section.InvalidDistanceException;
import wooteco.subway.line.exception.section.SameStationsInSameSectionException;
import wooteco.subway.station.domain.Station;

public class Section {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section() {
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validSection(upStation, downStation, distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        validSection(upStation, downStation, distance);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validSection(Station upStation, Station downStation, int distance) {
        validDistance(distance);
        validStationStationEquivalency(upStation, downStation);
    }

    private void validDistance(int distance) {
        if (distance <= 0) {
            throw new InvalidDistanceException();
        }
    }

    private void validStationStationEquivalency(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SameStationsInSameSectionException();
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
