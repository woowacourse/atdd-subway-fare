package wooteco.subway.path.domain.finder;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.application.PathException;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayRoute;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class DijkstraPathFinder implements PathFinder {

    @Override
    public SubwayRoute find(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new PathException("탐색 시작 역과 끝 역이 같을 수 없습니다.");
        }

        SubwayGraph graph = new SubwayGraph(SectionEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new PathException("적절하지 않은 구간의 경로 탐색입니다.");
        }

        return new SubwayRoute(new ArrayList<>(path.getEdgeList()), path.getVertexList());
    }
}
