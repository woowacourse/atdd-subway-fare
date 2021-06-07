package wooteco.subway.path.domain.finder;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayRoute;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    SubwayRoute find(List<Line> lines, Station source, Station target);
}
