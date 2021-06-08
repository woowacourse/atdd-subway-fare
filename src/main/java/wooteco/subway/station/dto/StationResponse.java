package wooteco.subway.station.dto;

import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationResponse {
    private Long id;
    private String name;
    private List<SimpleLineResponse> includedLines;

    public static StationResponse of(Station station, List<SimpleLineResponse> includedLines) {
        return new StationResponse(station.getId(), station.getName(), includedLines);
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<SimpleLineResponse> getIncludedLines() {
        return includedLines;
    }
}
