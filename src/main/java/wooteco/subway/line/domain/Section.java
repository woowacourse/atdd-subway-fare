package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

public class Section {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section() {
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
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

    public int getDistance() {
        return distance;
    }

    public boolean isBefore(Section section) {
        return downStation.equals(section.getUpStation());
    }

    public boolean isAfter(Section section) {
        return upStation.equals(section.getDownStation());
    }

    public boolean isUpstationEqualsTo(Station station) {
        return upStation.equals(station);
    }

    public boolean isDownStationEqualsTo(Station station) {
        return downStation.equals(station);
    }

    public boolean isShorterThan(Section section) {
        return distance <= section.getDistance();
    }
}
