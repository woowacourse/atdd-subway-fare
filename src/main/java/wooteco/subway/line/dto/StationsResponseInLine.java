package wooteco.subway.line.dto;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationsResponseInLine {
    private Long id;
    private String name;
    private int distance;
    private List<TransferLinesResponse> transferLines;

    public StationsResponseInLine(Long id, String name, int distance, List<TransferLinesResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.transferLines = transferLines;
    }

    public static StationsResponseInLine of(Station station, int nextStationDistance, List<TransferLinesResponse> transferLinesResponse) {
        return new StationsResponseInLine(station.getId(), station.getName(), nextStationDistance, transferLinesResponse);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public List<TransferLinesResponse> getTransferLines() {
        return transferLines;
    }
}
