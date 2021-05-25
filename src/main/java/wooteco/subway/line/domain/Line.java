package wooteco.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import wooteco.subway.exception.HttpException;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.station.domain.Station;

public class Line {
    private static final String NAME_PATTERN = "^[가-힣0-9]{2,10}$";

    private final Long id;
    private final String name;
    private final String color;
    private final Fare extraFare;
    private final Sections sections;

    public Line(Long id, String name, String color, Fare extraFare, Sections sections) {
        validate(name);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(String name, String color) {
        this(null, name, color, new Fare(0), new Sections());
    }

    public Line(Long id, String name, String color) {
        this(null, name, color, new Fare(0), new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, new Fare(0), sections);
    }

    public Line(String name, String color, Fare extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, new Fare(extraFare), new Sections());
    }

    public Line(Long id, String name, String color, Fare extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(String name, String color, int extraFare, Section initSection) {
        this(null, name, color, new Fare(extraFare), new Sections(initSection));
    }

    private void validate(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "노선 이름은 " + NAME_PATTERN + " 형식이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);
        sections.addSection(section);
    }

    public void addSection(Section section) {
        if (section == null) {
            return;
        }
        sections.addSection(section);
    }

    public void removeSection(Station station) {
        sections.removeStation(station);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Fare getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
