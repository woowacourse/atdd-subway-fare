package wooteco.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.domain.Station;

@Service
public class PathFinder {

    public SubwayPath findPath(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException("출발역과 도착역이 동일하여 경로를 찾을 수 없습니다.");
        }
        SubwayGraph graph = new SubwayGraph(SectionEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (Objects.isNull(path)) {
            throw new InvalidPathException("경로가 존재하지 않습니다.");
        }

        return convertSubwayPath(path);
    }

    private SubwayPath convertSubwayPath(GraphPath<Station, SectionEdge> graphPath) {
        List<SectionEdge> edges = new ArrayList<>(graphPath.getEdgeList());
        List<Station> stations = graphPath.getVertexList();
        return new SubwayPath(edges, stations);
    }
}
