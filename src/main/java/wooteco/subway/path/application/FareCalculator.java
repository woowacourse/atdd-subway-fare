package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.FareByDistance;
import wooteco.subway.path.domain.FareDiscountByAge;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Optional;

@Service
public class FareCalculator {
    public int calculateFare(Optional<LoginMember> loginMember, SubwayPath subwayPath) {
        final int fareByDistance = calculateFareByDistance(subwayPath);
        final int lineExtraFare = calculateLineExtraFare(subwayPath);
        final int totalFare = fareByDistance + lineExtraFare;
        return applyDiscountByMemberAge(loginMember, totalFare);
    }

    private int calculateFareByDistance(SubwayPath subwayPath) {
        final int totalDistance = subwayPath.calculateDistance();
        return FareByDistance.calculate(totalDistance);
    }

    private int calculateLineExtraFare(SubwayPath subwayPath) {
        return subwayPath.getSectionEdges()
                .stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElseThrow(FareCalculationException::new);
    }

    private int applyDiscountByMemberAge(Optional<LoginMember> loginMember, int totalFare) {
        return loginMember.map(member -> FareDiscountByAge.calculate(member.getAge(), totalFare))
                .orElse(totalFare);
    }
}
