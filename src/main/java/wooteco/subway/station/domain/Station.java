package wooteco.subway.station.domain;

import wooteco.subway.exception.InvalidInsertException;

import java.util.Objects;

public class Station {
    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void validateDifferentStation(Station downStation) {
        if (this.equals(downStation)) {
            throw new InvalidInsertException("유효하지 않는 요청 값입니다");
        }
    }
}
