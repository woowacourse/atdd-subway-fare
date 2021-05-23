package wooteco.subway.line.ui.dto;

import java.util.List;

public class SectionsOfLineResponse {

    private final Long id;
    private final String color;
    private final String name;

    private final List<StationOfLineResponse> stations;
    private final List<SectionResponse> sections;

    public SectionsOfLineResponse(LineWithTransferLineResponse lineResponse,
        SectionsResponse sectionsResponse) {
        this.id = lineResponse.getId();
        this.color = lineResponse.getColor();
        this.name = lineResponse.getName();

        this.stations = lineResponse.getStationResponses();
        this.sections = sectionsResponse.getSectionResponses();
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<StationOfLineResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }

}
