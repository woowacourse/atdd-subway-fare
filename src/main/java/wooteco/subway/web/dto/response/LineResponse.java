package wooteco.subway.web.dto.response;

import wooteco.subway.domain.Line;
import wooteco.subway.web.dto.LineResponseAssembler;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationWithDistanceResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationWithDistanceResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        return LineResponseAssembler.assemble(line);
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
