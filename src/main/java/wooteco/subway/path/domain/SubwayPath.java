package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.creationstrategy.DistanceBasedCreationStrategy;
import wooteco.subway.path.domain.fare.creationstrategy.FareCreationStrategy;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        validateSize(sectionEdges, stations);
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    private void validateSize(List<SectionEdge> sectionEdges, List<Station> stations) {
        if (sectionEdges.size() != stations.size() - 1) {
            throw new ValidationFailureException("경로의 구간의 수는 역의 수보다 하나 작아야 합니다.");
        }
    }

    public Fare calculateFare(FareCreationStrategy fareCreationStrategy) {
        return fareCreationStrategy.generate(calculateDistance())
            .addFare(
                new Fare(maximumLineExtraFare())
            );
    }

    public int calculateDistance() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getSectionDistance)
            .sum();
    }

    private int maximumLineExtraFare() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getLineExtraFare)
            .max()
            .orElse(0);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}
