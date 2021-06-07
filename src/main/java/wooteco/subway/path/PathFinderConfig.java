package wooteco.subway.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.path.domain.finder.DijkstraPathFinder;
import wooteco.subway.path.domain.finder.PathFinder;

@Configuration
public class PathFinderConfig {

    @Bean
    public PathFinder pathFinder() {
        return new DijkstraPathFinder();
    }
}
