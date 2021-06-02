package wooteco.subway.station.dto;

import wooteco.subway.line.dto.LineNameColorResponse;

import java.util.ArrayList;
import java.util.List;

public class StationTransferLinesDto {
    private Long id;
    private String name;
    private List<LineNameColorResponse> transferLines;

    public StationTransferLinesDto() {
    }

    public StationTransferLinesDto(Long id, String name) {
        this(id, name, new ArrayList<>());
    }

    public StationTransferLinesDto(Long id, String name, List<LineNameColorResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.transferLines = transferLines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LineNameColorResponse> getTransferLines() {
        return transferLines;
    }
}
