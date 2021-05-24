package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;

public class MapResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationOfMapResponse> stations;

    @ConstructorProperties({"id", "name", "color", "stations"})
    public MapResponse(Long id,
                       String name,
                       String color,
                       List<StationOfMapResponse> stations) {
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

    public List<StationOfMapResponse> getStations() {
        return stations;
    }
}
