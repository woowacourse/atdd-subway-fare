package wooteco.subway.line.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationWithDistanceResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color,
        List<StationWithDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<StationWithDistanceResponse> stations = new ArrayList<>();
        List<Section> sections = line.getSortedSections();
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            stations.add(new StationWithDistanceResponse(
                section.getUpStationId(), section.getUpStationName(), section.getDistance()));

            if (i == sections.size() - 1) {
                stations.add(new StationWithDistanceResponse(
                    section.getDownStationId(), section.getDownStationName()
                ));
            }
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public static List<LineResponse> listOf(List<Line> lines) {
        return lines.stream()
            .map(LineResponse::of)
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

    public List<StationWithDistanceResponse> getStations() {
        return stations;
    }
}
