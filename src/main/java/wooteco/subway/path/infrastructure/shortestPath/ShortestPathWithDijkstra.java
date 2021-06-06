package wooteco.subway.path.infrastructure.shortestPath;

import java.util.List;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.infrastructure.SectionEdge;
import wooteco.subway.path.infrastructure.SubwayGraph;
import wooteco.subway.station.domain.Station;

public class ShortestPathWithDijkstra extends ShortestPath {
    public ShortestPathWithDijkstra(List<Line> lines, Station source, Station target) {
        super(lines, source, target);
    }

    @Override
    protected ShortestPathAlgorithm<Station, SectionEdge> shortestPathAlgorithm(SubwayGraph subwayGraph) {
        return new DijkstraShortestPath<>(subwayGraph);
    }
}
