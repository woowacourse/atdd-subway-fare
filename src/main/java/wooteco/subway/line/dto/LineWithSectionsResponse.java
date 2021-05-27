package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineWithSectionsResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private List<Section> sections;

    public LineWithSectionsResponse() {

    }

    public LineWithSectionsResponse(Long id, String name, String color, List<StationResponse> stations, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineWithSectionsResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineWithSectionsResponse(line.getId(), line.getName(), line.getColor(), stations, line.getSections().getSections());
    }

    public static List<LineWithSectionsResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LineWithSectionsResponse::of)
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public LineResponse toLineResponse() {
        return new LineResponse(id, name, color, stations);
    }
}
