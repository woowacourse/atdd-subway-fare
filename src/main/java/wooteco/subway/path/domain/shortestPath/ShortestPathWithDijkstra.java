package wooteco.subway.path.domain.shortestPath;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import wooteco.subway.exception.invalid.InvalidPathException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.station.domain.Station;

public class ShortestPathWithDijkstra extends ShortestPath {
    public ShortestPathWithDijkstra(List<Line> lines, Station source, Station target) {
        super(lines, source, target);
    }

    @Override
    protected ShortestPathAlgorithm<Station, SectionEdge> getShortestPathAlgorithm(SubwayGraph subwayGraph) {
        return new DijkstraShortestPath<>(subwayGraph);
    }
}
