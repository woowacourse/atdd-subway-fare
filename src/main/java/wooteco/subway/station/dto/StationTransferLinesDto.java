package wooteco.subway.station.dto;

import wooteco.subway.line.dto.LineNameColorDto;

import java.util.ArrayList;
import java.util.List;

public class StationTransferLinesDto {
    private Long id;
    private String name;
    private List<LineNameColorDto> transferLines;

    public StationTransferLinesDto() {
    }

    public StationTransferLinesDto(Long id, String name) {
        this.id = id;
        this.name = name;
        this.transferLines = new ArrayList<>();
    }

    public StationTransferLinesDto(Long id, String name, List<LineNameColorDto> transferLines) {
        this.id = id;
        this.name = name;
        this.transferLines = transferLines;
    }

    public void addTransferLine(LineNameColorDto lineNameColorDto) {
        transferLines.add(lineNameColorDto);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LineNameColorDto> getTransferLines() {
        return transferLines;
    }
}
