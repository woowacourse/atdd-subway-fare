package wooteco.subway.station.dto;

import java.util.List;
import wooteco.subway.line.dto.TransferLineResponse;

public class StationMapResponse {
    private Long id;
    private String name;
    private int distance;
    private List<TransferLineResponse> transferLines;

    public StationMapResponse(Long id, String name, int distance,
        List<TransferLineResponse> transferLines) {
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

    public int getDistance() {
        return distance;
    }

    public List<TransferLineResponse> getTransferLines() {
        return transferLines;
    }
}
