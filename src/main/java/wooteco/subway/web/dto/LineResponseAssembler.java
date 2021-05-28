package wooteco.subway.web.dto;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.StationWithDistanceResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponseAssembler {
    public static LineResponse assemble(Line line) {
        List<Section> sortedSections = line.getSections().getSortedSections();
        List<StationWithDistanceResponse> stations = new ArrayList<>();

        for (int i = 0; i < sortedSections.size(); i++) {
            Station upStation = sortedSections.get(i).getUpStation();
            stations.add(new StationWithDistanceResponse(upStation.getId(),
                    upStation.getName(), sortedSections.get(i).getDistance()));
            if (i == sortedSections.size() - 1) {
                Station downStation = sortedSections.get(i).getDownStation();
                stations.add(new StationWithDistanceResponse(downStation.getId(),
                        downStation.getName()));
            }
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}