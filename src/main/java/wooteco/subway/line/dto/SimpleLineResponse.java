package wooteco.subway.line.dto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class SimpleLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public SimpleLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static List<SimpleLineResponse> listOf(List<Line> simpleLines) {
        return simpleLines.stream()
            .map(line -> new SimpleLineResponse(line.getId(), line.getName(), line.getColor(), StationResponse.listOf(line.getStations()))
            )
            .sorted(Comparator.comparing(SimpleLineResponse::getId))
            .collect(Collectors.toList());
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
