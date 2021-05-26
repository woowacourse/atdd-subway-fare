package wooteco.subway.station.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.station.exception.SubwayStationException;

public class Station {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]{2,20}$");

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
        this.name = name.trim();
    }

    private void validate(String name) {
        if (Objects.isNull(name) || !PATTERN.matcher(name.trim()).matches()) {
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
}
