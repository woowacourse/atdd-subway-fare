package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.exception.LineNotFoundException;
import wooteco.subway.path.domain.AgePolicy;
import wooteco.subway.path.domain.fare.FareChain;
import wooteco.subway.path.domain.fare.FirstFare;
import wooteco.subway.path.domain.fare.SecondFare;
import wooteco.subway.path.domain.fare.ThirdFare;

import java.util.List;

@Service
public class FareService {

    private FareChain basicFareChain;

    public FareService() {
        this.basicFareChain = new FirstFare();
        FareChain secondFareChain = new SecondFare();
        FareChain thirdFareChain = new ThirdFare();

        basicFareChain.setNextChainByChain(secondFareChain);
        secondFareChain.setNextChainByChain(thirdFareChain);
    }

    public int calculateFare(int distance) {
        return basicFareChain.calculateFare(distance);
    }

    public int calculateExtraFare(int fare, List<Line> lines) {
        return fare + lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new LineNotFoundException("노선이 존재하지 않습니다."));
    }

    public int discountFareByAge(int fare, int age) {
        final AgePolicy agePolicy = AgePolicy.of(age);
        return agePolicy.calculateFareAppliedAgePolicy(fare);
    }
}
