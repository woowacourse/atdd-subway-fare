package wooteco.subway.acceptance.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.acceptance.line.domain.Line;
import wooteco.subway.acceptance.path.domain.SectionEdge;
import wooteco.subway.acceptance.path.domain.SubwayGraph;
import wooteco.subway.acceptance.path.domain.SubwayPath;
import wooteco.subway.acceptance.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException();
        }

        return convertSubwayPath(path);
    }

    private SubwayPath convertSubwayPath(GraphPath<Station, SectionEdge> graphPath) {
        List<SectionEdge> edges = (List<SectionEdge>) new ArrayList<>(graphPath.getEdgeList());
        Set<Line> lines = edges.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
        List<Station> stations = graphPath.getVertexList();
        return new SubwayPath(edges, stations, lines);
    }
}
