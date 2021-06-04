package wooteco.subway.line.dto;

import java.util.List;
import wooteco.subway.station.dto.StationWithTransferAndNextDistanceResponse;

public class LineWithTransferAndDistanceResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationWithTransferAndNextDistanceResponse> stations;

    public LineWithTransferAndDistanceResponse() {
    }

    public LineWithTransferAndDistanceResponse(Long id, String name, String color,
        List<StationWithTransferAndNextDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public LineWithTransferAndDistanceResponse(Long id, String name, String color, int extraFare,
        List<StationWithTransferAndNextDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
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

    public List<StationWithTransferAndNextDistanceResponse> getStations() {
        return stations;
    }
}
