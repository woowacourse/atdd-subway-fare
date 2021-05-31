package wooteco.subway.line.domain;

import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.station.domain.Station;

public class Section {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validateStations(upStation, downStation);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateStations(Station upStation, Station downStation) {
        if (upStation.isSameName(downStation)) {
            throw new DuplicatedException("상행역과 하행역이 같을 수 없습니다.");
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
