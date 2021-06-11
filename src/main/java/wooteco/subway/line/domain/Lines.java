package wooteco.subway.line.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.exception.DuplicateLineNameException;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.path.domain.Fare;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void validateDuplicate(Line line) {
        if (lines.stream().anyMatch(line::isDuplicate)) {
            throw new DuplicateLineNameException();
        }
    }

    public List<Line> sortedByName() {
        return lines.stream()
            .sorted(Comparator.comparing(Line::getName))
            .collect(Collectors.toList());
    }

    public Fare getMaxExtraFare() {
        return new Fare(getMaxExtraFareValue());
    }

    private int getMaxExtraFareValue() {
        return lines.stream()
            .mapToInt(Line::getExtraFare)
            .max()
            .orElseThrow(NoSuchLineException::new);
    }
}
