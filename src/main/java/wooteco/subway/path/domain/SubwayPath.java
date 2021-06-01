package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;
import wooteco.subway.member.domain.authmember.AuthMember;
import wooteco.subway.path.domain.CalculateAdditionalFare.AdditionalFareOver10Km;
import wooteco.subway.path.domain.CalculateAdditionalFare.AdditionalFareOver50km;
import wooteco.subway.path.domain.CalculateAdditionalFare.AdditionalFareStrategy;
import wooteco.subway.path.domain.CalculateAdditionalFare.AdditionalFareUnder10km;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;
    private static final List<AdditionalFareStrategy> additionalFareStrategies = Arrays.asList(
        new AdditionalFareUnder10km(),
        new AdditionalFareOver10Km(),
        new AdditionalFareOver50km()
    );

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = new ArrayList<>(sectionEdges);
        this.stations = new ArrayList<>(stations);
    }

    public List<SectionEdge> getSectionEdges() {
        return Collections.unmodifiableList(sectionEdges);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare(int distance, AuthMember authMember) {
        int fare = DEFAULT_FARE;
        fare += calculateAdditionalFare(distance);
        fare += calculateAdditionalFareByLine();
        fare = authMember.discountFareByAge(fare);

        return fare;
    }

    private int calculateAdditionalFare(int distance) {
        return distinguishAdditionalFareStrategy(distance).calculateAdditionalFare(distance);
    }

    private AdditionalFareStrategy distinguishAdditionalFareStrategy(int distance) {
        return additionalFareStrategies.stream()
            .filter(additionalFareStrategy -> additionalFareStrategy.isInDistanceRange(distance))
            .findAny()
            .orElseThrow(NotExistException::new);
    }

    private int calculateAdditionalFareByLine() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getExtraFare)
            .max()
            .getAsInt();
    }
}
