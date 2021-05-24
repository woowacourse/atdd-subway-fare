package wooteco.subway.line.dto;

import java.util.List;

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
