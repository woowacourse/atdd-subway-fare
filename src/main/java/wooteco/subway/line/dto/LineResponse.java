package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.station.dto.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;
    private List<SectionResponse> sections;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Fare extraFare, List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare.getFare();
        this.stations = stations;
        this.sections = sections;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stationResponses = getStationResponses(line);
        List<SectionResponse> sectionResponses = SectionResponse.of(line.getSections().getSections());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stationResponses, sectionResponses);
    }

    private static List<StationResponse> getStationResponses(Line line) {
        return line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
