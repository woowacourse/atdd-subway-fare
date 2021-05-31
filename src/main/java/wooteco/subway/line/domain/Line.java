package wooteco.subway.line.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

public class Line {

    private Long id;
    private String name;
    private String color;
    private int fare;
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, int fare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(Long id, String name, String color, int fare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.fare = fare;
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

    public int getFare() {
        return fare;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Section> getSectionGroup() {
        return sections.getSections();
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
