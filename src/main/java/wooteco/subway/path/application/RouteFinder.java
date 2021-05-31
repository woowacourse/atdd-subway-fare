package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayRoute;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteFinder {
    public SubwayRoute find(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new PathException("적절하지 않은 구간의 경로 탐색입니다.");
        }
        SubwayGraph graph = new SubwayGraph(SectionEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new PathException("적절하지 않은 구간의 경로 탐색입니다.");
        }

        return convertSubwayPath(path);
    }

    private SubwayRoute convertSubwayPath(GraphPath graphPath) {
        List<SectionEdge> edges = (List<SectionEdge>) graphPath.getEdgeList()
                .stream()
                .collect(Collectors.toList());
        List<Station> stations = graphPath.getVertexList();

        return new SubwayRoute(edges, stations);
    }
}
