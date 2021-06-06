package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.ui.dto.map.StationOfSectionResponse;

public class SectionResponse {

    private final StationOfSectionResponse upStation;
    private final StationOfSectionResponse downStation;
    private final int distance;

    @ConstructorProperties({"upStation", "downStation", "distance"})
    public SectionResponse(StationOfSectionResponse upStation,
        StationOfSectionResponse downStation,
        int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SectionResponse(Section section) {
        this.upStation = new StationOfSectionResponse(section.getUpStation());
        this.downStation = new StationOfSectionResponse(section.getDownStation());
        this.distance = section.getDistance();
    }

    public StationOfSectionResponse getUpStation() {
        return upStation;
    }

    public StationOfSectionResponse getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

}
