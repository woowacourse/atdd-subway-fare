package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class SimpleLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public SimpleLineResponse() {
    }

    public SimpleLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static SimpleLineResponse of(Line line) {
        List<StationResponse> stations = StationResponse.listOf(line.getStations());

        return new SimpleLineResponse(line.getId(), line.getName(), line.getColor(), stations);
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
