package wooteco.subway.line.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private Long id;
    private LineName lineName;
    private LineColor lineColor;
    private Integer extraFare;
    private Sections sections;

    public Line() {
    }

    public Line(Long id, String lineName, String lineColor, int extraFare, Sections sections) {
        this.id = id;
        this.lineName = new LineName(lineName);
        this.lineColor = new LineColor(lineColor);
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(String lineName, String lineColor) {
        this(null, lineName, lineColor, 0, new Sections());
    }

    public Line(String lineName, String lineColor, int extraFare) {
        this(null, lineName, lineColor, extraFare, new Sections());
    }

    public Line(Long id, String lineName, String lineColor) {
        this(id, lineName, lineColor, 0, new Sections());
    }

    public Line(Long id, String lineName, String lineColor, int extraFare) {
        this(id, lineName, lineColor, extraFare, new Sections());
    }

    public boolean containsStation(Station station) {
        return sections.containsStation(station);
    }

    public boolean hasSameName(String name) {
        return this.lineName.hasSameName(name);
    }

    public boolean hasSameColor(String color) {
        return this.lineColor.hasSameColor(color);
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

    public String getLineName() {
        return lineName.getName();
    }

    public String getLineColor() {
        return lineColor.getColor();
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }
}
