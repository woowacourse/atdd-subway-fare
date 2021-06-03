package wooteco.subway.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.path.domain.fare.distance.DefaultDistance;
import wooteco.subway.path.domain.fare.distance.DistanceChain;
import wooteco.subway.path.domain.fare.distance.SecondDistance;
import wooteco.subway.path.domain.fare.distance.ThirdDistance;

@Configuration
public class DistanceChainConfig {
    private static final int FIRST_THRESHOLD = 10;
    private static final int SECOND_THRESHOLD = 50;

    @Bean
    public DistanceChain defaultChain() {
        DistanceChain thirdChain = new ThirdDistance();
        DistanceChain secondChain = new SecondDistance(thirdChain, SECOND_THRESHOLD - FIRST_THRESHOLD);
        return new DefaultDistance(secondChain, FIRST_THRESHOLD);
    }
}
