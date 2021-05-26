package wooteco.subway.line.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.exception.LineException;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]{1,10}$");

    private Long id;
    private String name;
    private String color;
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(0L, name, color, new Sections());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        validate(name, color);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private void validate(String name, String color) {
        validateNameLength(name);
        validateColor(color);
    }

    private void validateColor(String color) {
        if(Objects.isNull(color) || color.length() == 0) {
            throw new SubwayCustomException(LineException.INVALID_LINE_COLOR_EXCEPTION);
        }
    }

    private void validateNameLength(String name) {
        if(Objects.isNull(name)) {
            throw new SubwayCustomException(LineException.INVALID_LINE_EXCEPTION_EXCEPTION);
        }

        if(!isNamePattern(name)) {
            throw new SubwayCustomException(LineException.INVALID_LINE_EXCEPTION_EXCEPTION);
        }
    }

    private boolean isNamePattern(String name) {
        Matcher m = PATTERN.matcher(name);
        return m.matches();
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

    public List<Section> getListSections() {
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
