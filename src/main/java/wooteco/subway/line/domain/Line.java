package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private static final Long NONE_EXTRA_FARE = 0L;

    private Long id;
    private String name;
    private String color;
    private Long extraFare;
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(0L, name, color, NONE_EXTRA_FARE, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, NONE_EXTRA_FARE, new Sections());
    }

    public Line(Long id, String name, String color, Long extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(String name, String color, Long extraFare) {
        this(0L, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, NONE_EXTRA_FARE, sections);
    }

    public Line(Long id, String name, String color, Long extraFare, Sections sections) {
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

    public List<Section> sections() {
        return sections.getSections();
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

    public Long getExtraFare() {
        return extraFare;
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
