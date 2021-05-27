package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationWithTransferLinesAndNextDistanceResponse;

import java.util.List;

public class LineWithTransferLinesAndNextDistanceResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationWithTransferLinesAndNextDistanceResponse> stations;

    public LineWithTransferLinesAndNextDistanceResponse(Long id, String name, String color,
                                                        List<StationWithTransferLinesAndNextDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineWithTransferLinesAndNextDistanceResponse of(Line line,
                                                                  List<StationWithTransferLinesAndNextDistanceResponse> stations) {
        return new LineWithTransferLinesAndNextDistanceResponse(line.getId(), line.getName(), line.getColor(), stations);
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

    public List<StationWithTransferLinesAndNextDistanceResponse> getStations() {
        return stations;
    }
}
