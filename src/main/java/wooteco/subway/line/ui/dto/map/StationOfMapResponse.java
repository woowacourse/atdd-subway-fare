package wooteco.subway.line.ui.dto.map;

import java.beans.ConstructorProperties;
import java.util.List;
import wooteco.subway.line.ui.dto.sectionsofline.TransferLineResponse;

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
