package wooteco.subway.station.dto;

import java.util.List;
import wooteco.subway.line.dto.LineWithoutSectionsResponse;

public class StationWithTransferAndNextDistanceResponse {

    private Long id;
    private String name;
    private int distance;
    private List<LineWithoutSectionsResponse> transferLines;

    public StationWithTransferAndNextDistanceResponse() {
    }

    public StationWithTransferAndNextDistanceResponse(Long id, String name, Integer distance,
        List<LineWithoutSectionsResponse> transferLines) {
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

    public List<LineWithoutSectionsResponse> getTransferLines() {
        return transferLines;
    }
}
