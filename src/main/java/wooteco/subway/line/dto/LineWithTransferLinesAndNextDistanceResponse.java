package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationWithTransferLinesAndNextDistanceResponse;

import java.util.List;

public class LineWithTransferLinesAndNextDistanceResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationWithTransferLinesAndNextDistanceResponse> stations;

    public LineWithTransferLinesAndNextDistanceResponse() {
    }

    public LineWithTransferLinesAndNextDistanceResponse(Long id, String name, String color,
                                                        List<StationWithTransferLinesAndNextDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public LineWithTransferLinesAndNextDistanceResponse(Long id, String name, String color, int extraFare,
                                                        List<StationWithTransferLinesAndNextDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationWithTransferLinesAndNextDistanceResponse> getStations() {
        return stations;
    }
}
