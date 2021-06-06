package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.domain.User;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.FareByDistance;
import wooteco.subway.path.domain.FareDiscountByAge;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.exception.FareCalculationException;

@Service
public class FareCalculator {
    public int calculateFare(User user, SubwayPath subwayPath) {
        final int fareByDistance = calculateFareByDistance(subwayPath);
        final int lineExtraFare = calculateLineExtraFare(subwayPath);
        final int totalFare = fareByDistance + lineExtraFare;
        return applyDiscountByMemberAge(user, totalFare);
    }

    private int calculateFareByDistance(SubwayPath subwayPath) {
        final int totalDistance = subwayPath.calculateDistance();
        return FareByDistance.calculate(totalDistance);
    }

    private int calculateLineExtraFare(SubwayPath subwayPath) {
        return subwayPath.getSectionEdges()
                .stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(FareCalculationException::new);
    }

    private int applyDiscountByMemberAge(User user, int totalFare) {
        if (user.isLoggedIn()) {
            return FareDiscountByAge.calculate(user.getAge(), totalFare);
        }
        return totalFare;
    }
}
