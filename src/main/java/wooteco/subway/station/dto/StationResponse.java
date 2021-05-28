package wooteco.subway.station.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.station.domain.Station;

public class StationResponse {

    private Long id;
    private String name;
    private List<SimpleLineResponse> lines;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponse(Long id, String name, List<SimpleLineResponse> lineResponses) {
        this.id = id;
        this.name = name;
        this.lines = lineResponses;
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public static StationResponse of(Station station, List<SimpleLineResponse> lineResponses) {
        return new StationResponse(station.getId(), station.getName(), lineResponses);
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
