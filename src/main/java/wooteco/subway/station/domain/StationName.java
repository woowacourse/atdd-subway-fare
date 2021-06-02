package wooteco.subway.station.domain;

import wooteco.subway.station.exception.InvalidStationNameException;

import java.util.regex.Pattern;

public class StationName {
    private static final String NAME_PATTERN = "^[가-힣|0-9]*$";
    private static final String BLANK = " ";
    private static final int MINIMUM_NAME_LENGTH = 2;

    private final String name;

    public StationName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        validateNameLength(name);
        validateNameContainsBlank(name);
        validateNameMatchesPattern(name);
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH) {
            throw new InvalidStationNameException();
        }
    }

    private void validateNameContainsBlank(String name) {
        if (name.contains(BLANK)) {
            throw new InvalidStationNameException();
        }
    }

    private void validateNameMatchesPattern(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
            throw new InvalidStationNameException();
        }
    }

    public String getName() {
        return name;
    }
}
