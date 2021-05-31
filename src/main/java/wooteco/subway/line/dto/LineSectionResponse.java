package wooteco.subway.line.dto;

import wooteco.subway.station.dto.StationTransferResponse;

import java.util.List;

public class LineSectionResponse {
    private long id;
    private String name;
    private String color;
    private List<StationTransferResponse> stations;
    private List<SectionResponse> sections;

    public LineSectionResponse() {
    }

    public LineSectionResponse(final long id,
                               final String name,
                               final String color,
                               final List<StationTransferResponse> stations,
                               final List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineSectionResponse of(final long id,
                                         final String name,
                                         final String color,
                                         final List<StationTransferResponse> stations,
                                         final List<SectionResponse> sections) {
        return new LineSectionResponse(id, name, color, stations, sections);
    }

    public long getId() {
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
