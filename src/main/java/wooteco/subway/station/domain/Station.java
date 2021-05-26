package wooteco.subway.station.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.station.exception.StationException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Station {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]{2,20}$");


    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
    }

    private void validateNameLength(String name) {
        if (Objects.isNull(name)) {
            throw new SubwayCustomException(StationException.INVALID_STATION_NAME_LENGTH_EXCEPTION);
        }

        name = name.trim();

        if(!isNamePattern(name)) {
            throw new SubwayCustomException(StationException.INVALID_STATION_NAME_LENGTH_EXCEPTION);
        }
    }

    private boolean isNamePattern(String name) {
        Matcher m = PATTERN.matcher(name);
        return m.matches();
    }

    public Station(String name) {
        this(0L, name);
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
