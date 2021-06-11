package wooteco.subway.station.dto;

import wooteco.subway.line.dto.LineWithTransferLinesResponse;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationWithTransferLinesAndNextDistanceResponse {
    private final Long id;
    private final String name;
    private final int distance;
    private final List<LineWithTransferLinesResponse> transferLines;

    public StationWithTransferLinesAndNextDistanceResponse(Long id, String name,
                                                           int distance, List<LineWithTransferLinesResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.transferLines = transferLines;
    }

    public static StationWithTransferLinesAndNextDistanceResponse of(Station station, int distance,
                                                                     List<LineWithTransferLinesResponse> transferLines) {
        return new StationWithTransferLinesAndNextDistanceResponse(station.getId(), station.getName(), distance, transferLines);
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

    public List<LineWithTransferLinesResponse> getTransferLines() {
        return transferLines;
    }
}
