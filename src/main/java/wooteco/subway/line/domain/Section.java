package wooteco.subway.line.domain;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.HttpException;
import wooteco.subway.station.domain.Station;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Long id, Station upStation, Station downStation, int distance) {
        validate(upStation.getId(), downStation.getId());
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(null, upStation, downStation, distance);
    }

    private void validate(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "상행 종점, 하행 종점은 같을 수 없습니다.");
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

    public Long getUpStationId() {
        return upStation.getId();
    }
}
