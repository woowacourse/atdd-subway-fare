package wooteco.subway.path.domain.policy;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.path.domain.policy.discountpolicy.ChildrenDiscountPolicy;
import wooteco.subway.path.domain.policy.discountpolicy.DiscountFarePolicy;
import wooteco.subway.path.domain.policy.discountpolicy.TeenageDiscountPolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.DefaultExtraFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.ExtraFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.FiftyOverPolicyExtra;
import wooteco.subway.path.domain.policy.extrafarepolicy.TenFiftyPolicyExtra;

public class FarePolicyFactory {

    public static List<ExtraFarePolicy> createExtraFarePolicy() {
        return Arrays.asList(
            new DefaultExtraFarePolicy(),
            new TenFiftyPolicyExtra(),
            new FiftyOverPolicyExtra()
        );
    }

    public static List<DiscountFarePolicy> createDiscountPolicy() {
        return Arrays.asList(
            new ChildrenDiscountPolicy(),
            new TeenageDiscountPolicy()
        );
    }

}
