package wooteco.subway.line.ui.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StationOfMapResponse {

    private final Long id;
    private final String name;
    private final int distance;
    private final List<TransferLineResponse> transferLineResponses;

    @ConstructorProperties({"id", "name", "transferLineResponses"})
    public StationOfMapResponse(Long id,
                                String name,
                                int distance,
                                List<TransferLineResponse> transferLineResponses) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.transferLineResponses = transferLineResponses;
    }

    public StationOfMapResponse(Station station, int distance, List<Line> lines) {
        this.id = station.getId();
        this.name = station.getName();
        this.distance = distance;
        this.transferLineResponses = lines.stream()
                .map(TransferLineResponse::new)
                .collect(toList());
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

    public List<TransferLineResponse> getTransferLineResponses() {
        return transferLineResponses;
    }

}
