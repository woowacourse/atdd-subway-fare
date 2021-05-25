package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class LineWithSectionsResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stations;
    private final List<SectionResponse> sections;

    public LineWithSectionsResponse(Long id, String name, String color, int extraFare,
        List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
        this.sections = sections;
    }

    public static LineWithSectionsResponse of(Line line, List<SectionResponse> sections) {
        List<StationResponse> stations = line.getStations().stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());
        return new LineWithSectionsResponse(line.getId(), line.getName(), line.getColor(),
            line.getExtraFare(),
            stations, sections);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
