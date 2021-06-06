package wooteco.subway.line.domain;

import wooteco.subway.line.exception.InvalidLineColorException;

public class LineColor {
    private static final String BLANK = " ";

    private final String color;

    public LineColor(String color) {
        validateColorContainsBlank(color);
        this.color = color;
    }

    private void validateColorContainsBlank(String color) {
        if (color.contains(BLANK)) {
            throw new InvalidLineColorException();
        }
    }

    public boolean hasSameColor(String color) {
        return this.color.equals(color);
    }

    public String getColor() {
        return color;
    }
}
