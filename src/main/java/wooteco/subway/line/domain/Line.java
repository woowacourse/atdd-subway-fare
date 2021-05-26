package wooteco.subway.line.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.exception.SubwayLineException;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Line {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]{2,10}$");

    private Long id;
    private String name;
    private String color;
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        validate(name, color);
        this.id = id;
        this.name = name.trim();
        this.color = color;
        this.sections = sections;
    }

    private void validate(String name, String color) {
        validateName(name);
//        validateColor(color);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || !PATTERN.matcher(name.trim()).matches()) {
            throw new SubwayCustomException(SubwayLineException.INVALID_LINE_EXCEPTION);
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

    public List<Station> getStations() {
        return sections.getStations();
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
}
