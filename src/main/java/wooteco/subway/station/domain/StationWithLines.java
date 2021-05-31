package wooteco.subway.station.domain;

import wooteco.subway.line.domain.Line;

import java.util.List;

public class StationWithLines extends Station {
    private final List<Line> lines;

    public StationWithLines(final Long id, final String name, final List<Line> lines) {
        super(id, name);
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }
}
