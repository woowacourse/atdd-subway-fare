package wooteco.subway.path.domain.policy;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.path.domain.policy.discountpolicy.ChildrenAgeFarePolicy;
import wooteco.subway.path.domain.policy.discountpolicy.DiscountPolicy;
import wooteco.subway.path.domain.policy.discountpolicy.TeenageAgeFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.DefaultExtraFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.ExtraFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.FiftyOverPolicyExtraFare;
import wooteco.subway.path.domain.policy.extrafarepolicy.TenFiftyPolicyExtraFare;

public class FarePolicyFactory {

    public static List<ExtraFarePolicy> createExtraFarePolicy() {
        return Arrays.asList(
            new DefaultExtraFarePolicy(),
            new TenFiftyPolicyExtraFare(),
            new FiftyOverPolicyExtraFare()
        );
    }

    public static List<DiscountPolicy> createDiscountPolicy() {
        return Arrays.asList(
            new ChildrenAgeFarePolicy(),
            new TeenageAgeFarePolicy()
        );
    }

}
