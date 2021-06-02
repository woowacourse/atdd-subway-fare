package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private final Long id;
    private final String name;
    private final String color;
    private final Integer extraFare;
    private final Sections sections;

    public Line(String name, String color) {
        this(null, name, color, null, new Sections());
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, null, new Sections());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new Sections());

    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, null, sections);
    }

    public Line(Long id, String name, String color, Integer extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
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

    public int getExtraFare() {
        return extraFare;
    }
}
