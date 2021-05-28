package wooteco.subway.web.dto;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.StationWithDistanceResponse;

public class LineResponseAssembler {

    public static LineResponse assemble(Line line) {
        final ArrayList<StationWithDistanceResponse> stationResponses = new ArrayList<>();
        final List<Section> sections = line.sortedSections();
        for (int i = 0; i < sections.size(); i++) {
            if (i == sections.size() - 1) {
                final Section lastSection = sections.get(i);
                final Station upStation = lastSection.getUpStation();
                final Station downStation = lastSection.getDownStation();
                stationResponses.add(new StationWithDistanceResponse(upStation.getId(),
                    upStation.getName(), lastSection.getDistance()));
                stationResponses.add(new StationWithDistanceResponse(downStation.getId(),
                    downStation.getName()));
                break;
            }
            final Section section = sections.get(i);
            final Station upStation = section.getUpStation();
            stationResponses.add(new StationWithDistanceResponse(upStation.getId(),
                upStation.getName(), section.getDistance()));
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
