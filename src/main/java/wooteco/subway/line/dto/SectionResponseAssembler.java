package wooteco.subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SectionResponseAssembler {
    public static SectionResponse assemble(Section section, Station station, List<Line> linesByStationId) {
        List<TransferLineResponse> lineResponses = linesByStationId.stream()
            .map(line -> new TransferLineResponse(line.getId(), line.getName(), line.getColor()))
            .collect(Collectors.toList());
        return new SectionResponse(station.getId(), station.getName(), section.getDistance(), lineResponses);
    }
}
