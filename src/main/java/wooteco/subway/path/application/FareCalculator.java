package wooteco.subway.path.application;

import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Comparator;
import java.util.List;

@Component
public class FareCalculator implements FarePolicy {
    public Fare calculateTotalFare(SubwayPath subwayPath, LoginMember loginMember) {
        Fare fareByDistance = getFareByDistance(subwayPath.calculateDistance());
        Fare fareWithLineExtraFare = getFareWithLineExtraFare(fareByDistance, subwayPath.getLines());

        if (!loginMember.isLoggedIn()) {
            return fareWithLineExtraFare;
        }
        return AgeDiscountPolicy.discountFareByAge(loginMember.getAge(), fareWithLineExtraFare);
    }

    public Fare getFareByDistance(int distance) {
        return DistanceExtraFarePolicy.calculateFareByDistance(distance);
    }

    public Fare getFareWithLineExtraFare(Fare currentFare, List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(Fare::getFare))
                .orElseThrow(() -> new IllegalArgumentException("Line이 한 개 이상 존재해야 합니다."))
                .add(currentFare);
    }
}
