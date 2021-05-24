package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayException;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Sections.empty());
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);
        sections.addSection(section);
    }

    public void addSection(Section section) {
        if(Objects.isNull(section)) {
            throw new SubwayException("구간 추가에 실패했습니다");
        }

        sections.addSection(section);
    }

    public void updateSectionDistance(Long upStationId, Long downStationId, Integer distance) {
        sections.updateDistance(upStationId, downStationId, distance);
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

    public Sections getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && name.equals(line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
