package wooteco.subway.line.ui.dto.sectionsofline;

import wooteco.subway.line.ui.dto.SectionResponse;
import wooteco.subway.line.ui.dto.SectionsResponse;

import java.beans.ConstructorProperties;
import java.util.List;

public class SectionsOfLineResponse {

    private final Long id;
    private final String color;
    private final String name;

    private final List<StationOfLineResponse> stations;
    private final List<SectionResponse> sections;

    @ConstructorProperties({"id", "color", "name", "stations", "sections"})
    public SectionsOfLineResponse(Long id, String color, String name,
        List<StationOfLineResponse> stations,
        List<SectionResponse> sections) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.stations = stations;
        this.sections = sections;
    }

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
