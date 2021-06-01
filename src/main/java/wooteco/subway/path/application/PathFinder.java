package wooteco.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.exception.invalid.InvalidPathException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.shortestPath.ShortestPath;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.shortestPath.ShortestPathWithDijkstra;
import wooteco.subway.station.domain.Station;

@Service
public class PathFinder {
    public SubwayPath findPath(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException();
        }

        ShortestPath shortestPath = new ShortestPathWithDijkstra(lines, source, target);
        return new SubwayPath(shortestPath.getSections(), shortestPath.getStations(), 0);
    }
}