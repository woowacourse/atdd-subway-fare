package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLinesContainStation(Station station) {
        final ArrayList<Line> lines = new ArrayList<>();
        for (Line line : this.lines) {
            if (line.getStations().contains(station)) {
                lines.add(line);
            }
        }
        return lines;
    }
}
