package wooteco.subway.path.domain;

import wooteco.subway.path.domain.strategy.additional.AgeDiscountPolicies;
import wooteco.subway.path.domain.strategy.discount.DistanceAdditionFactory;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance())
                .sum();
    }

    public int calculateFare(Integer age) {
        Fare fare = new Fare(DistanceAdditionFactory.create(calculateDistance(), DEFAULT_FARE),
                AgeDiscountPolicies.instanceOf(age),
                findMaximumExtraFare()
        );
        return fare.calculateFare();
    }

    private int findMaximumExtraFare() {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }
}
