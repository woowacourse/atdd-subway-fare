package wooteco.subway.section.domain;

import wooteco.subway.exception.SameStationsInSameSectionException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

public class Section {

    private final Id id;
    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Long id, Section section) {
        this(new Id(id), section.line, section.upStation, section.downStation, section.distance);
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(Id id, Line line, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        validateDuplicateStations(this.upStation, this.downStation);
    }

    private void validateDuplicateStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SameStationsInSameSectionException();
        }
    }

    public Section updateForSave(Section section) {
        Distance updateDistance = this.distance.subtract(section.distance);

        if (upStation.equals(section.upStation)) {
            return new Section(null, line, section.downStation, downStation, updateDistance);
        }
        return new Section(null, line, upStation, section.upStation, updateDistance);
    }

    public Section updateForDelete(Section section) {
        Distance updateDistance = section.distance.add(this.distance);
        return new Section(null, line, upStation, section.downStation, updateDistance);
    }

    public boolean hasSameStationBySection(Section section) {
        return hasSameStation(section.getUpStation()) ||
            hasSameStation(section.getDownStation());
    }

    public boolean hasSameStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean isMatchUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isMatchDownStation(Station station) {
        return downStation.equals(station);
    }

    public Long getId() {
        return id.getValue();
    }

    public Line getLine() {
        return line;
    }

    public Long getLineId() {
        return line.getId();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public String getUpStationName() {
        return upStation.getName();
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }
}
