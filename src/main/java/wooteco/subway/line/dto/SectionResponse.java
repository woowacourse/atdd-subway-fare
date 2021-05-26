package wooteco.subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dto.StationResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SectionResponse {
    private StationResponse upStation;
    private StationResponse downStation;
    private Integer distance;

    public static SectionResponse of(Section section) {
        StationResponse upStation = StationResponse.of(section.getUpStation());
        StationResponse downsStation = StationResponse.of(section.getDownStation());
        Integer distance = section.getDistance();

        return new SectionResponse(upStation, downsStation, distance);
    }
}

