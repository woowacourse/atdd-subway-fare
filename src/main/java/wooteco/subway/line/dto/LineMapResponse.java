package wooteco.subway.line.dto;

import wooteco.subway.station.dto.StationMapResponse;

import java.util.List;

public class LineMapResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationMapResponse> stations;

    public LineMapResponse() {
    }

    public LineMapResponse(final Long id, final String name, final String color, final List<StationMapResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public List<StationMapResponse> getStations() {
        return stations;
    }
}
