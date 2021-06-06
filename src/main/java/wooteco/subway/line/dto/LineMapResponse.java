package wooteco.subway.line.dto;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationMapResponse;

public class LineMapResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationMapResponse> stations;

    private LineMapResponse(Long id, String name, String color,
        List<StationMapResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineMapResponse of(Line line, List<StationMapResponse> stations) {
        return new LineMapResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public static LineMapResponse of(Long id, String name, String color, List<StationMapResponse> stations) {
        return new LineMapResponse(id, name, color, stations);
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
