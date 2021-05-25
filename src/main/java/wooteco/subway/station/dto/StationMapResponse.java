package wooteco.subway.station.dto;

import wooteco.subway.line.dto.TransferLineResponse;

import java.util.List;

public class StationMapResponse {
    private Long id;
    private String name;
    private Integer distance;
    private List<TransferLineResponse> transferLines;

    public StationMapResponse() {
    }

    public StationMapResponse(final Long id, final String name, final Integer distance, final List<TransferLineResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.transferLines = transferLines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<TransferLineResponse> getTransferLines() {
        return transferLines;
    }
}
