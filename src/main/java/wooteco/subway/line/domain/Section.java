package wooteco.subway.line.domain;

import wooteco.subway.line.exception.InvalidSectionRequestException;
import wooteco.subway.station.domain.Station;

public class Section {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public static class Builder {
        private Long id;
        private Station upStation;
        private Station downStation;
        private final int distance;

        public Builder(Long id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        public Builder(int distance) {
            this.distance = distance;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Section build() {
            validate(this.upStation, this.downStation);
            return new Section(this);
        }
    }

    private Section(Builder builder) {
        this.id = builder.id;
        this.upStation = builder.upStation;;
        this.downStation = builder.downStation;
        this.distance = builder.distance;
    }

//    private Section()
//
//    public Section(Long id, Station upStation, Station downStation, int distance) {
//        this.id = id;
//        this.upStation = upStation;
//        this.downStation = downStation;
//        this.distance = distance;
//    }
//
//    public Section(Station upStation, Station downStation, int distance) {
//        validate(upStation, downStation);
//        this.upStation = upStation;
//        this.downStation = downStation;
//        this.distance = distance;
//    }

    private static void validate(Station upStation, Station downStation) {
        if (upStation.hasEqualIdWith(downStation)) {
            throw new InvalidSectionRequestException("상행역과 하행역은 같을 수 없습니다.");
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
