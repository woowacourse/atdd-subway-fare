package wooteco.subway.line.ui.dto;

import static java.util.stream.Collectors.toList;

import java.beans.ConstructorProperties;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

public class StationOfLineResponse {

    private final Long id;
    private final String name;
    private final List<TransferLineResponse> transferLineResponses;

    @ConstructorProperties({"id", "name", "transferLineResponses"})
    public StationOfLineResponse(Long id, String name,
        List<TransferLineResponse> transferLineResponses) {
        this.id = id;
        this.name = name;
        this.transferLineResponses = transferLineResponses;
    }

    public StationOfLineResponse(Station station, List<Line> lines) {
        this.id = station.getId();
        this.name = station.getName();

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

    public List<TransferLineResponse> getTransferLineResponses() {
        return transferLineResponses;
    }

}
