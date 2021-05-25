package wooteco.subway.path.domain.farepolicy;

import java.util.Arrays;
import java.util.List;

public class FarePolicyFactory {

    public static List<FarePolicy> create() {
        return Arrays.asList(
            new DefaultFarePolicy(),
            new TenFiftyPolicy(),
            new FiftyOverPolicy()
        );
    }

}
