package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class SectionResponse {
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(Station upStation, Station downStation, int distance) {
        this.upStation = StationResponse.of(upStation);
        this.downStation = StationResponse.of(downStation);
        this.distance = distance;
    }

    public static List<SectionResponse> of(List<Section> sections) {
        return sections.stream()
            .map(section -> new SectionResponse(section.getUpStation(), section.getDownStation(), section.getDistance()))
            .collect(Collectors.toList());
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
