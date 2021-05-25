package wooteco.subway.station.dto;

import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationLineResponse {
    private Long id;
    private String name;
    private List<TransferLineResponse> lines;

    public StationLineResponse() {
    }

    public StationLineResponse(final Long id, final String name, final List<TransferLineResponse> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TransferLineResponse> getLines() {
        return lines;
    }
}
