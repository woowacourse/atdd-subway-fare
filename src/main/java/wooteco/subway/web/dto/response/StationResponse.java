package wooteco.subway.web.dto.response;

import wooteco.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {

    private Long id;
    private String name;
    private List<SimpleLineResponse> lines;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static StationResponse of(Station station, List<SimpleLineResponse> linesResponses){
        return new StationResponse(station.getId(), station.getName(), linesResponses);
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
