package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationTransferLinesResponse;
import wooteco.subway.station.dto.StationTransferLinesDto;

import java.util.List;
import java.util.stream.Collectors;

public class LineStationWithTransferLinesResponse {
    private Long id;
    private String name;
    private String color;
    private Integer extraFare;
    private List<StationTransferLinesResponse> stations;

    public LineStationWithTransferLinesResponse() {
    }

    public LineStationWithTransferLinesResponse(Long id, String name, String color, List<StationTransferLinesResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public LineStationWithTransferLinesResponse(Long id, String name, String color, Integer extraFare, List<StationTransferLinesResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineStationWithTransferLinesResponse of(Line line, List<StationTransferLinesDto> allStationWithTransferLines) {
        List<Section> sections = line.getSortSections();
        List<StationTransferLinesResponse> stationTransferLinesResponses = sections.stream()
                .map(section -> StationTransferLinesResponse.of(line.getId(), section, allStationWithTransferLines))
                .collect(Collectors.toList());
        Section lastSection = sections.get(sections.size() - 1);
        stationTransferLinesResponses.add(StationTransferLinesResponse.of(line.getId(), lastSection.getDownStation(), allStationWithTransferLines));
        return new LineStationWithTransferLinesResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stationTransferLinesResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public List<StationTransferLinesResponse> getStations() {
        return stations;
    }
}
