package wooteco.subway.line.dto;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, null);
    }

    public LineResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
    }

    public static LineResponse of(Line line, List<StationResponse> stationResponses) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stationResponses);
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
}
