package wooteco.subway.web.dto.response;

import wooteco.subway.domain.Section;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;

    public static SectionResponse of(Section section) {
        return new SectionResponse(section);
    }

    public SectionResponse() {
    }

    public SectionResponse(StationResponse upStation, StationResponse downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public SectionResponse(Section section) {
        this(section.getId(),
                StationResponse.of(section.getUpStation()),
                StationResponse.of(section.getDownStation()),
                section.getDistance());
    }

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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
