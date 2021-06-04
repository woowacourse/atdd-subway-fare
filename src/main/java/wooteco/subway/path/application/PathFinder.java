package wooteco.subway.path.application;

import static wooteco.subway.exception.SubwayExceptions.*;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.infrastructure.SubwayPath;
import wooteco.subway.path.infrastructure.shortestPath.ShortestPath;
import wooteco.subway.path.infrastructure.shortestPath.ShortestPathWithDijkstra;
import wooteco.subway.station.domain.Station;

@Service
public class PathFinder {
    public SubwayPath findPath(List<Line> lines, Station source, Station target) {
        if (lines.isEmpty() || source.equals(target)) {
            throw INVALID_PATH.makeException();
        }

        ShortestPath shortestPath = new ShortestPathWithDijkstra(lines, source, target);
        return new SubwayPath(shortestPath.getSections(), shortestPath.getStations(), 0);
    }
}