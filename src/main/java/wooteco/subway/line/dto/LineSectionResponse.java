package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;

public class LineSectionResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationTransferResponse> stations;
    private List<SectionResponse> sections;

    public LineSectionResponse() {
    }

    public LineSectionResponse(Long id, String name, String color,
        List<StationTransferResponse> stations,
        List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineSectionResponse of(Line line,
        List<StationTransferResponse> stations) {
        return new LineSectionResponse(line.getId(), line.getName(), line.getColor(),
            sortStations(line, stations), convertToSectionResponse(line.getSections().sort()));
    }

    private static List<StationTransferResponse> sortStations(Line line,
        List<StationTransferResponse> stationResponses) {
        return line.getStations().stream()
            .map(station -> stationResponses.stream()
                .filter(response -> response.getId().equals(station.getId()))
                .findFirst()
                .get())
            .collect(Collectors.toList());
    }

    private static List<SectionResponse> convertToSectionResponse(Sections sections) {
        return sections.getSections().stream()
            .map(SectionResponse::of)
            .collect(Collectors.toList());
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

    public List<StationTransferResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
