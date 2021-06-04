package wooteco.subway.line.domain;

import java.util.Objects;
import wooteco.subway.path.domain.Fare;

public class Line {

    private final Id id;
    private final Name name;
    private final Color color;
    private final Fare extraFare;

    public Line(String name, String color, int extraFare) {
        this(null, new Name(name), new Color(color), new Fare(extraFare));
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(new Id(id), new Name(name), new Color(color), new Fare(extraFare));
    }

    public Line(Id id, Name name, Color color, Fare extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public boolean isDuplicate(Line line) {
        return name.equals(line.name);
    }

    public Long getId() {
        return id.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public int getExtraFare() {
        return extraFare.getValue();
    }

    public boolean isNotEqual(Line line) {
        return !id.equals(line.id);
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name)
            && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
