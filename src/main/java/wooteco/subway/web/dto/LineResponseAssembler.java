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
            final Section section = sections.get(i);
            final Station upStation = section.getUpStation();
            stationResponses.add(new StationWithDistanceResponse(upStation.getId(),
                upStation.getName(), section.getDistance()));
            addLastStation(stationResponses, sections, i, section);
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private static void addLastStation(ArrayList<StationWithDistanceResponse> stationResponses,
        List<Section> sections, int index, Section section) {
        if (index == sections.size() - 1) {
            final Station downStation = section.getDownStation();
            stationResponses.add(new StationWithDistanceResponse(downStation.getId(),
                downStation.getName()));
        }
    }
}
