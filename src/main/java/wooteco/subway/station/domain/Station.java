package wooteco.subway.station.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.station.exception.SubwayStationException;

public class Station {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]*$");
    private static final int STATION_NAME_MIN_LENGTH = 2;
    private static final int STATION_NAME_MAX_LENGTH = 20;

    private Long id;
    private String name;

    public Station() {
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new SubwayCustomException(SubwayStationException.INVALID_STATION_NAME_EXCEPTION);
        }
        name = name.trim();
        if (name.length() < STATION_NAME_MIN_LENGTH
            || name.length() > STATION_NAME_MAX_LENGTH
            || !PATTERN.matcher(name).matches()) {
            throw new SubwayCustomException(SubwayStationException.INVALID_STATION_NAME_EXCEPTION);
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
