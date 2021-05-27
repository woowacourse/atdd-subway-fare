package wooteco.subway.line.domain;

import wooteco.subway.exception.InvalidInsertException;
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
        validateStations(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new InvalidInsertException("유효하지 않은 요청 값입니다");
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

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public int getDistance() {
        return distance;
    }

    public boolean isBefore(Section first) {
        return downStation.equals(first.getUpStation());
    }

    public boolean isAfter(Section last) {
        return upStation.equals(last.getDownStation());
    }

    public boolean isSameUpStation(Station station) {
        return upStation.equals(station);
    }
}
