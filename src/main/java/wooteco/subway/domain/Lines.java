package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLineContainStation(Station station) {
        return lines.stream()
                .filter(line -> line.getStations().contains(station))
                .collect(Collectors.toList());
    }
}
