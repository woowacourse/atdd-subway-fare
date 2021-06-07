package wooteco.subway.line.domain;

import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, LineRequest lineUpdateRequest) {
        this.id = id;
        this.name = lineUpdateRequest.getName();
        this.color = lineUpdateRequest.getColor();
        this.extraFare = lineUpdateRequest.getExtraFare();
    }

    public Line(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public boolean hasDifferentName(LineRequest lineUpdateRequest) {
        return !name.equals(lineUpdateRequest.getName());
    }

    public boolean hasDifferentColor(LineRequest lineUpdateRequest) {
        return !color.equals(lineUpdateRequest.getColor());
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

    public List<Section> getEachSection() {
        return sections.getSections();
    }

    public boolean hasStation(Long id) {
        return sections.getStations().stream()
                .anyMatch(station -> station.hasSameId(id));
    }

    public boolean hasSameName(String name) {
        return this.name.equals(name);
    }

    public boolean hasSameColor(String color) {
        return this.color.equals(color);
    }
}
