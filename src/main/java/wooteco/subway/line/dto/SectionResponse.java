package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class SectionResponse {
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    public SectionResponse(StationResponse upStation, StationResponse downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static List<SectionResponse> listOf(Sections sections) {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        for (Section section : sections.getSections()) {
            sectionResponses.add(new SectionResponse(
                    StationResponse.of(section.getUpStation()),
                    StationResponse.of(section.getDownStation()),
                    section.getDistance()
            ));
        }
        return sectionResponses;
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
