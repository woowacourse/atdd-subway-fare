package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayFare;
import wooteco.subway.path.strategy.FareStrategyFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FareService {

    public FareService() {

    }

    public SubwayFare findFare(List<SectionEdge> edge, LoginMember loginMember) {
        Set<Line> lines = edge.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
        return new SubwayFare(lines, FareStrategyFactory.findStrategy(loginMember.getAge()));
    }
}
