package wooteco.subway.line.domain;

import java.util.List;
import wooteco.subway.line.exception.WrongLineNameException;
import wooteco.subway.station.domain.Station;

public class Line {

    private Long id;
    private String name;
    private String color;
    private Integer extraFare;
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this(null, name, color, 0, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, 0, new Sections());
    }

    public Line(String name, String color, Integer extraFare) {
        this(null, name, color, extraFare, new Sections());
    }

    public Line(final Long id, final String name, final String color, final Integer extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color,0, sections);
    }

    public Line(final Long id, final String name, final String color, final Integer extraFare,
        final Sections sections) {
        checkNameConvention(name);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    private void checkNameConvention(String name) {
        String lastLetter = name.substring(name.length() - 1);
        if (!lastLetter.equals("선")) {
            throw new WrongLineNameException(name);
        }
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
