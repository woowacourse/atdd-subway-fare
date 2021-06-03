package wooteco.subway.section.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import wooteco.subway.exception.BothStationAlreadyRegisteredInLineException;
import wooteco.subway.exception.BothStationNotRegisteredInLineException;
import wooteco.subway.exception.NoSuchSectionException;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.exception.OnlyOneSectionExistsException;
import wooteco.subway.station.domain.Station;

public class Sections {

    private static final int MINIMUM_COUNT = 1;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isBothEndSection(Section section) {
        Deque<Station> stations = sortedStations();
        return section.isMatchDownStation(stations.peekFirst())
            || section.isMatchUpStation(stations.peekLast());
    }

    public boolean isBothEndStation(Station station) {
        Deque<Station> stations = sortedStations();
        return station.equals(stations.peekFirst())
            || station.equals(stations.peekLast());
    }

    public Deque<Station> sortedStations() {
        Deque<Station> stations = new ArrayDeque<>();
        Map<Station, Station> upStationIds = new LinkedHashMap<>();
        Map<Station, Station> downStationIds = new LinkedHashMap<>();

        initStations(stations, upStationIds, downStationIds);
        sortStations(stations, upStationIds, downStationIds);
        return new ArrayDeque<>(stations);
    }

    private void initStations(Deque<Station> stations, Map<Station, Station> upStations,
        Map<Station, Station> downStations) {

        for (Section section : sections) {
            upStations.put(section.getUpStation(), section.getDownStation());
            downStations.put(section.getDownStation(), section.getUpStation());
        }

        Section section = sections.get(0);
        stations.addFirst(section.getUpStation());
        stations.addLast(section.getDownStation());
    }

    private void sortStations(Deque<Station> stations, Map<Station, Station> upStations,
        Map<Station, Station> downStations) {

        while (upStations.containsKey(stations.peekLast())) {
            Station tailStation = stations.peekLast();
            stations.addLast(upStations.get(tailStation));
        }

        while (downStations.containsKey(stations.peekFirst())) {
            Station headStation = stations.peekFirst();
            stations.addFirst(downStations.get(headStation));
        }
    }

    public void validateInsertable(Section section) {
        boolean isUpStationNotExisted = isNotExistOnLine(section.getUpStation());
        boolean isDownStationNotExisted = isNotExistOnLine(section.getDownStation());

        if (isUpStationNotExisted && isDownStationNotExisted) {
            throw new BothStationNotRegisteredInLineException();
        }

        if (!isUpStationNotExisted && !isDownStationNotExisted) {
            throw new BothStationAlreadyRegisteredInLineException();
        }
    }

    public void validateDeletableCount() {
        if (sections.size() <= MINIMUM_COUNT) {
            throw new OnlyOneSectionExistsException();
        }
    }

    public void validateExistStation(Station station) {
        if (isNotExistOnLine(station)) {
            throw new NoSuchStationException();
        }
    }

    private boolean isNotExistOnLine(Station station) {
        return sections.stream()
            .noneMatch(section -> section.hasSameStation(station));
    }

    public boolean hasStation(Station station) {
        return sections.stream()
            .anyMatch(section -> section.hasSameStation(station));
    }

    public Section findByMatchStation(Section section) {
        return sections.stream()
            .filter(it -> section.isMatchUpStation(it.getUpStation()) || section.isMatchDownStation(it.getDownStation()))
            .findAny()
            .orElseThrow(NoSuchSectionException::new);
    }

    public int distanceValueWithNextStationOf(Station station) {
        return sections.stream()
            .filter(section -> section.isMatchUpStation(station))
            .map(Section::getDistanceValue)
            .findAny()
            .orElse(0);
    }

    public int totalDistanceValue() {
        return sections.stream()
            .mapToInt(Section::getDistanceValue)
            .sum();
    }

    public Station startStation() {
        return sortedStations().peekFirst();
    }

    public Station endStation() {
        return sortedStations().peekLast();
    }

    public List<Section> getSections() {
        return sections;
    }
}
