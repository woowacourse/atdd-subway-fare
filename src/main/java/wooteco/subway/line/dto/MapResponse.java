package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class MapResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final List<SectionResponse> sections;

    public MapResponse(Long id, String name, String color, List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public static MapResponse of(Line line) {
        return new MapResponse(line.getId(), line.getName(), line.getColor(),
                StationResponse.listOf(line.getStations()), SectionResponse.listOf(line.getSections()));
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
