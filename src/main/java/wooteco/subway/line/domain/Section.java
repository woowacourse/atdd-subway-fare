package wooteco.subway.line.domain;

import java.util.Objects;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.station.domain.Station;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validateThatHasNotSameStations(upStation, downStation);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(null, upStation, downStation, distance);
    }

    private void validateThatHasNotSameStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SubwayException("구간은 같은 역을 상행역과 하행역으로 가질 수 없습니다.");
        }
    }

    public boolean hasSameUpStationId(Long id) {
        return upStation.isSameId(id);
    }

    public boolean hasSameDownStationId(Long id) {
        return downStation.isSameId(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return distance == section.distance &&
            Objects.equals(id, section.id) &&
            Objects.equals(upStation, section.upStation) &&
            Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }

}
