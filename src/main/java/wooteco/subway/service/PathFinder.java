package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.common.exception.badrequest.InvalidPathException;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.SectionEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.domain.SubwayPath;

@Service
public class PathFinder {

    public SubwayPath findPath(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException();
        }
        SubwayGraph graph = new SubwayGraph(SectionEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException();
        }

        return convertSubwayPath(path);
    }

    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        List<SectionEdge> edges = (List<SectionEdge>) graphPath.getEdgeList().stream()
            .collect(Collectors.toList());
        List<Station> stations = graphPath.getVertexList();
        return new SubwayPath(edges, stations);
    }
}
