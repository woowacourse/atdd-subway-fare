package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private Long id;
    private String name;
    private String color;
    private Sections sections = new Sections();
    private int extraFare;

    public Line() {
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections(), 0);
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, new Sections(), extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, new Sections(), extraFare);
    }

    public Line(Long id, String name, String color, Sections sections, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.extraFare = extraFare;
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

    public List<Section> sections() {
        return sections.getSections();
    }

    public int getExtraFare() {
        return extraFare;
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

    public int distance() {
        return sections.totalDistance();
    }
}
