package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;
import wooteco.subway.exception.SubwayException;

public abstract class DistanceExtraFarePolicy implements ExtraFarePolicy {

    @Override
    public BigDecimal calculate(int distance) {
        if (!isSatisfied(distance)) {
            throw new SubwayException("사용할 수 없는 요금 정책입니다.");
        }

        return calculateExtraFare(distance);
    }

    protected abstract BigDecimal calculateExtraFare(int distance);
}
