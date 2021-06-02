package wooteco.subway.web.dto.response;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;
    private List<SimpleLineResponse> lines = new ArrayList<>();

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static StationResponse of(Station station, List<Line> lines) {
        List<SimpleLineResponse> simpleLines = lines.stream()
                .map(line -> new SimpleLineResponse(line.getId(), line.getName(), line.getColor()))
                .collect(Collectors.toList());
        return new StationResponse(station.getId(), station.getName(), simpleLines);
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponse(Long id, String name, List<SimpleLineResponse> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public static List<StationResponse> listOf(List<Station> stations, Lines lines) {
        return stations.stream()
                .map(station -> StationResponse.of(station, lines.getLineContainStation(station)))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SimpleLineResponse> getLines() {
        return lines;
    }
}
