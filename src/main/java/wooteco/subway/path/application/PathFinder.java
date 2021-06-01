package wooteco.subway.path.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.LineExtraFareCalculator;
import wooteco.subway.path.domain.fare.age.AgeFare;
import wooteco.subway.path.domain.fare.age.AgeFarePolicy;
import wooteco.subway.path.domain.fare.distance.DistanceFare;
import wooteco.subway.path.domain.fare.distance.DistanceFarePolicy;
import wooteco.subway.station.domain.Station;

@Service
public class PathFinder {
    private static final int DEFAULT_FARE = 1250;

    private final LineExtraFareCalculator lineExtraFareCalculator;

    public PathFinder() {
        this.lineExtraFareCalculator = new LineExtraFareCalculator();
    }

    public SubwayPath findPath(List<Line> lines, Station source, Station target, Optional<LoginMember> loginMember) {
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

        SubwayPath subwayPathBeforeCalculateFare = convertSubwayPath(path);
        Fare totalFare = getTotalFare(loginMember, subwayPathBeforeCalculateFare);
        return new SubwayPath(subwayPathBeforeCalculateFare, totalFare);
    }

    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        List<SectionEdge> edges = (List<SectionEdge>) graphPath.getEdgeList().stream().collect(Collectors.toList());
        List<Station> stations = graphPath.getVertexList();
        return new SubwayPath(edges, stations);
    }

    private Fare getTotalFare(Optional<LoginMember> loginMemberOptional, SubwayPath subwayPath) {
        Fare fareByDistance = getFareByDistance(subwayPath);
        Fare fareWithLineExtraFare = lineExtraFareCalculator.getFare(fareByDistance, subwayPath.getLines());
        if (!loginMemberOptional.isPresent()) {
            return fareWithLineExtraFare;
        }
        LoginMember loginMember = loginMemberOptional.get();
        return getFareByAge(fareWithLineExtraFare, loginMember);
    }

    private Fare getFareByAge(Fare fareWithLineExtraFare, LoginMember loginMember) {
        AgeFare ageFare = AgeFarePolicy.getAgeFareByAge(loginMember.getAge());
        return ageFare.getFare(fareWithLineExtraFare);
    }

    private Fare getFareByDistance(SubwayPath subwayPath) {
        int distance = subwayPath.getTotalDistance();
        DistanceFare distanceFare = DistanceFarePolicy.getDistanceFareByDistance(distance);
        return distanceFare.getFare(new Fare(DEFAULT_FARE));
    }
}
