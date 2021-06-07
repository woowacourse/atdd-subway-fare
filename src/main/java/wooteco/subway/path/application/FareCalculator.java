package wooteco.subway.path.application;

import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class FareCalculator implements FarePolicy {
    private static final int DEFAULT_FARE = 1250;

    public Fare calculateTotalFare(SubwayPath subwayPath, LoginMember loginMember) {
        Fare fareByDistance = getFareByDistance(new Fare(DEFAULT_FARE), subwayPath.calculateDistance());
        Fare fareWithLineExtraFare = getFareWithLineExtraFare(fareByDistance, subwayPath.getLines());

        if (!loginMember.isLoggedIn()) {
            return fareWithLineExtraFare;
        }
        return AgeDiscountPolicy.discountFareByAge(loginMember.getAge(), fareWithLineExtraFare);
    }

    public Fare getFareByDistance(Fare currentFare, int distance) {
        if (distance <= 10) {
            return currentFare;
        }
        if (distance <= 50) {
            int extraDistance = distance - 10;
            int extraFare = (int) (Math.ceil(extraDistance / 5.0) * 100);
            return getFareByDistance(currentFare, 10).add(new Fare(extraFare));
        }
        int extraDistance = distance - 50;
        int extraFare = (int) (Math.ceil(extraDistance / 8.0) * 100);
        return getFareByDistance(currentFare, 50).add(new Fare(extraFare));
    }


    public Fare getFareWithLineExtraFare(Fare currentFare, List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(Fare::getFare))
                .orElseThrow(() -> new IllegalArgumentException("Line이 한 개 이상 존재해야 합니다."))
                .add(currentFare);
    }
}
