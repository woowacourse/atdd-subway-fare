package wooteco.subway.station.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.domain.StationWithLines;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<StationLineResponse> listOf(final List<StationWithLines> stationsWithLines) {
        return stationsWithLines.stream()
                .map(it -> {
                    List<Line> lines = it.getLines();
                    List<TransferLineResponse> transferLineResponses = TransferLineResponse.listOf(lines);
                    return new StationLineResponse(it.getId(), it.getName(), transferLineResponses);
                })
                .collect(Collectors.toList());
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
