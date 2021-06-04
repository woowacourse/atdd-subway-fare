package wooteco.subway.station.domain;

import java.util.Objects;
import wooteco.subway.station.exception.WrongStationNameException;

public class Station {

    private Long id;
    private String name;

    public Station() {
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        checkNameConvention(name);
        this.id = id;
        this.name = name;
    }

    private void checkNameConvention(String name) {
        String lastLetter = name.substring(name.length() - 1);
        if (!lastLetter.equals("역")) {
            throw new WrongStationNameException(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
