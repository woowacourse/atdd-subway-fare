package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.FareDiscountByAge;
import wooteco.subway.path.domain.FareByDistance;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Objects;

@Service
public class FareCalculator {
    public int calculateFare(LoginMember loginMember, SubwayPath subwayPath) {
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
                .orElseThrow(IllegalStateException::new);
    }

    private int applyDiscountByMemberAge(LoginMember loginMember, int totalFare) {
        if (Objects.isNull(loginMember.getAge())) {
            return totalFare;
        }
        return FareDiscountByAge.calculate(loginMember.getAge(), totalFare);
    }
}
