package wooteco.subway.line.ui.dto.map;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.ui.dto.sectionsofline.TransferLineResponse;
import wooteco.subway.station.domain.Station;

import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StationOfMapResponse {

    private final Long id;
    private final String name;
    private final int distance;
    private final List<TransferLineResponse> transferLines;

    @ConstructorProperties({"id", "name", "transferLines"})
    public StationOfMapResponse(Long id,
                                String name,
                                int distance,
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
