package wooteco.subway.station.dto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.station.domain.Station;

public class StationResponse {
    private Long id;
    private String name;
    private List<SimpleLineResponse> includedLines;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                       .map(StationResponse::of)
                       .collect(Collectors.toList());
    }

    public static StationResponse of(Station station, List<Line> persistLines) {
        final List<SimpleLineResponse> simpleLineResponses =
                persistLines.stream()
                            .filter(line -> line.isIncludingStation(station.getId()))
                            .map(SimpleLineResponse::new)
                            .sorted(Comparator.comparing(SimpleLineResponse::getName))
                            .collect(Collectors.toList());

        return new StationResponse(station.getId(), station.getName(), simpleLineResponses);
    }

    public List<SimpleLineResponse> getIncludedLines() {
        return includedLines;
    }

    public StationResponse(Long id, String name, List<SimpleLineResponse> includedLines) {
        this.id = id;
        this.name = name;
        this.includedLines = includedLines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
