package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;

public class LineWithTransferLinesAndStationsResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationsResponseInLine> stations;

    public LineWithTransferLinesAndStationsResponse(Long id, String name,
                                                    String color,
                                                    List<StationsResponseInLine> stationsResponseInLine) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stationsResponseInLine;
    }

    public static LineWithTransferLinesAndStationsResponse of(Line line, List<StationsResponseInLine> response) {
        return new LineWithTransferLinesAndStationsResponse(line.getId(), line.getName(), line.getColor(), response);
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

    public List<StationsResponseInLine> getStations() {
        return stations;
    }
}
