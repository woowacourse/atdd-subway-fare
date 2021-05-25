package wooteco.subway.station.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import wooteco.subway.exception.HttpException;

public class Station {
    private static final String NAME_PATTERN = "^[가-힣0-9]{2,20}$";

    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this(null, name);
    }

    private void validate(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "역 이름은 " + NAME_PATTERN + " 형식이어야 합니다.");
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
