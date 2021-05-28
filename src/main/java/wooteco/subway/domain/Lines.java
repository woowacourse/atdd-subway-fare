package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLineContainStation(Station station) {
        List<Line> containStationLines = new ArrayList<>();
        for (Line line : lines) {
            if (line.getStations().contains(station)) {
                containStationLines.add(line);
            }
        }
        return containStationLines;
    }
}
