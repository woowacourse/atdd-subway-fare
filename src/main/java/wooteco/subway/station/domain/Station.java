package wooteco.subway.station.domain;

import wooteco.subway.station.exception.InvalidStationNameException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Station {
    private static String NAME_PATTERN = "^[가-힣|0-9]*$";

    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        validateNameLength(name);
        validateNameContainsBlank(name);
        validateNameMatchesPattern(name);
    }

    private void validateNameLength(String name) {
        if (name.length() < 2) {
            throw new InvalidStationNameException();
        }
    }

    private void validateNameContainsBlank(String name) {
        if (name.contains(" ")) {
            throw new InvalidStationNameException();
        }
    }

    private void validateNameMatchesPattern(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
            throw new InvalidStationNameException();
        }
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
}
