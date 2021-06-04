package wooteco.subway.station.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineNameColorResponse;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationTransferLinesResponse {
    private Long id;
    private String name;
    private int distance;
    private List<LineNameColorResponse> transferLines;

    public StationTransferLinesResponse() {
    }

    public StationTransferLinesResponse(Long id, String name, int distance, List<LineNameColorResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.transferLines = transferLines;
    }

    public static StationTransferLinesResponse of(Long id, Section section, List<StationTransferLinesDto> allStationWithTransferLines) {
        StationTransferLinesDto stationTransferLinesDto = allStationWithTransferLines.stream()
                .filter(stationWithTransferLine -> section.getUpStation().getId().equals(stationWithTransferLine.getId()))
                .findAny().orElseThrow(IllegalArgumentException::new);
        List<LineNameColorResponse> lineNameColorResponses = stationTransferLinesDto.getTransferLines().stream()
                .filter(lineNameColorResponse -> !lineNameColorResponse.getId().equals(id))
                .collect(Collectors.toList());
        return new StationTransferLinesResponse(stationTransferLinesDto.getId(), stationTransferLinesDto.getName(), section.getDistance(), lineNameColorResponses);
    }

    public static StationTransferLinesResponse lastStationResponse(Long lineId, Station downStation, List<StationTransferLinesDto> allStationWithTransferLines) {
        StationTransferLinesDto stationTransferLinesDto = allStationWithTransferLines.stream()
                .filter(stationWithTransferLine -> downStation.getId().equals(stationWithTransferLine.getId()))
                .findAny().orElseThrow(IllegalArgumentException::new);
        List<LineNameColorResponse> lineNameColorResponses = stationTransferLinesDto.getTransferLines().stream()
                .filter(lineNameColorResponse -> !lineNameColorResponse.getId().equals(lineId))
                .collect(Collectors.toList());
        return new StationTransferLinesResponse(downStation.getId(), downStation.getName(), 0, lineNameColorResponses);
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

    public List<LineNameColorResponse> getTransferLines() {
        return transferLines;
    }
}
