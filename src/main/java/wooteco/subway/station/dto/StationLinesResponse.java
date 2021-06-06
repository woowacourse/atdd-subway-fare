package wooteco.subway.station.dto;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.domain.Station;

public class StationLinesResponse {
    private Long id;
    private String name;
    private List<TransferLineResponse> lines;

    public StationLinesResponse(Long id, String name,
        List<TransferLineResponse> lines) {
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

    public static StationLinesResponse of(Station station,
        Map<List<Station>, Line> lineStations) {
        return new StationLinesResponse(station.getId(), station.getName(), convertToTransferLineResponse(station, lineStations));
    }

    private static List<TransferLineResponse> convertToTransferLineResponse(Station station,
        Map<List<Station>, Line> lineStations) {
        return lineStations.entrySet().stream()
            .filter(entry -> entry.getKey().contains(station))
            .map(Entry::getValue)
            .map(TransferLineResponse::of)
            .collect(Collectors.toList());
    }
}
