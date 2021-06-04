package wooteco.subway.line.domain;

import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.Money;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private Long id;
    private String name;
    private String color;
    private Fare extraFare;
    private Sections sections;

    public Line(String name, String color) {
        this(null, name, color, Fare.of(Money.ZERO), new Sections());
    }

    public Line(String name, String color, Fare extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Fare.of(Money.ZERO), new Sections());
    }

    public Line(Long id, String name, String color, Fare extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, Fare.of(Money.ZERO), sections);
    }

    public Line(Long id, String name, String color, Fare extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line update(String name, String color, Fare extraFare) {
        return new Line(this.id, name, color, extraFare, this.sections);
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

    public int moneyValue() {
        return extraFare.money();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSortSections() {
        return sections.getSortSections();
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

    public Fare getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }
}
