package wooteco.subway.line.ui.dto.sectionsofline;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StationOfLineResponse {

    private final Long id;
    private final String name;
    private final List<TransferLineResponse> transferLines;

    @ConstructorProperties({"id", "name", "transferLines"})
    public StationOfLineResponse(Long id, String name,
        List<TransferLineResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.transferLines = transferLines;
    }

    public StationOfLineResponse(Station station, List<Line> lines) {
        this.id = station.getId();
        this.name = station.getName();

        this.transferLines = lines.stream()
            .map(TransferLineResponse::new)
            .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TransferLineResponse> getTransferLines() {
        return transferLines;
    }

}
