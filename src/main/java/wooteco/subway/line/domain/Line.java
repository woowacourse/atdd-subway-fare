package wooteco.subway.line.domain;

import wooteco.subway.line.exception.InvalidLineColorException;
import wooteco.subway.line.exception.InvalidLineNameException;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.regex.Pattern;

public class Line {
    private static final String NAME_PATTERN = "^[가-힣|0-9]*$";

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this(null, name, color, 0, new Sections());
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, 0, new Sections());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
        this.id = id;
        validateName(name);
        this.name = name;
        validateColor(color);
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    private void validateName(String name) {
        validateNameLength(name);
        validateNameContainsBlank(name);
        validateNameMatchesPattern(name);
    }

    private void validateNameLength(String name) {
        if (name.length() < 2) {
            throw new InvalidLineNameException();
        }
    }

    private void validateNameContainsBlank(String name) {
        if (name.contains(" ")) {
            throw new InvalidLineNameException();
        }
    }

    private void validateNameMatchesPattern(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
            throw new InvalidLineNameException();
        }
    }

    private void validateColor(String color) {
        if (color.contains(" ")) {
            throw new InvalidLineColorException();
        }
    }

    public boolean containsStation(Station station) {
        return sections.containsStation(station);
    }

    public boolean hasSameName(String name) {
        return this.name.equals(name);
    }

    public boolean hasSameColor(String color) {
        return this.color.equals(color);
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }
}
