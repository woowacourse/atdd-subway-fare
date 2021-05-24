package wooteco.subway.station.ui.dto;

import java.util.List;

public class StationWithLinesResponse {

    private final Long id;
    private final String name;
    private final List<LineResponse> lines;

    public StationWithLinesResponse(Long id, String name, List<LineResponse> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LineResponse> getLines() {
        return lines;
    }

}
