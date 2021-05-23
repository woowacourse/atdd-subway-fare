package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private static final int NONE_EXTRA_FARE = 0;

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(0L, name, color, NONE_EXTRA_FARE, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, NONE_EXTRA_FARE, new Sections());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, NONE_EXTRA_FARE, sections);
    }

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
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

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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
}
