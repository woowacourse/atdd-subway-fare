package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.strategy.FareStrategyFactory;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PathFinder {
    public SubwayPath findPath(List<Line> lines, Station source, Station target, LoginMember loginMember) {
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

        return convertSubwayPath(path, loginMember.getAge());
    }

    private SubwayPath convertSubwayPath(GraphPath graphPath, int age) {
        List<SectionEdge> edges = (List<SectionEdge>) graphPath.getEdgeList().stream().collect(Collectors.toList());
        List<Station> stations = graphPath.getVertexList();
        Set<Line> line = edges.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
        return new SubwayPath(edges, stations, line, FareStrategyFactory.findStrategy(age));
    }
}
