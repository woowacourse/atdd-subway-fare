package wooteco.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.line.exception.SubwayLineException;
import wooteco.subway.station.domain.Station;

public class Line {

    private static final Pattern PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]{2,10}$");

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare, null);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections());
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new Sections());
    }

    public Line(Long id, String name, String color, Sections sections) {
        this(id, name, color, 0, sections);
    }

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
        validate(name);
        this.id = id;
        this.name = name.trim();
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    private void validate(String name) {
        validateName(name);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }
}
