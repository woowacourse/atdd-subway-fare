package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LinesResponse {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;
    private List<SectionResponse> sections;

    public LinesResponse(final Long id, final String name, final String color, final int distance, final List<StationResponse> stations, final List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
        this.sections = sections;
    }

    public static LinesResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());

        List<SectionResponse> sections = line.sections().stream()
                .map(it -> SectionResponse.of(it))
                .collect(Collectors.toList());

        // TODO : 전체 거리를 구하는 메소드 호출
        return new LinesResponse(line.getId(), line.getName(), line.getColor(), 0 , stations, sections);
    }

    public static List<LinesResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(LinesResponse::of)
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
}
