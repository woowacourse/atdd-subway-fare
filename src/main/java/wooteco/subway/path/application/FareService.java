package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayFare;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FareService {

    private FareFinder fareFinder;

    public FareService(FareFinder fareFinder) {
        this.fareFinder = fareFinder;
    }

    public SubwayFare findFare(List<SectionEdge> edge, int distance, LoginMember loginMember) {
        Set<Line> lines = edge.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
        return fareFinder.findFare(lines, distance, loginMember);
    }
}
