package wooteco.subway.line.domain;

import static wooteco.subway.exception.SubwayExceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.station.domain.Station;

public class Sections {
    private List<Station> orderedStations;
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Station> orderedStations, List<Section> sections) {
        this.orderedStations = orderedStations;
        this.sections = sections;
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
        this.orderedStations = makeOrderedStations();
    }

    public void addSection(Section section) {
        checkDifferentStations(section);

        if (this.sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        checkAlreadyExisted(section);
        checkExistedAny(section);

        addSectionUpToUp(section);
        addSectionDownToDown(section);

        this.sections.add(section);
    }

    private void checkAlreadyExisted(Section section) {
        if (!orderedStations.contains(section.getUpStation()) && !orderedStations.contains(section.getDownStation())) {
            throw BOTH_STATION_NOT_REGISTERED_IN_LINE.makeException();
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (orderedStations.containsAll(stationsOfNewSection)) {
            throw BOTH_STATION_ALREADY_REGISTERED_IN_LINE.makeException();
        }
    }

    private void checkDifferentStations(Section section) {
        if (section.getUpStation().equals(section.getDownStation())) {
            throw SAME_STATIONS_IN_SAME_SECTION.makeException();
        }
    }

    private void addSectionUpToUp(Section section) {
        this.sections.stream()
            .filter(it -> it.getUpStation().equals(section.getUpStation()))
            .findFirst()
            .ifPresent(it -> replaceSectionWithDownStation(section, it));
    }

    private void addSectionDownToDown(Section section) {
        this.sections.stream()
            .filter(it -> it.getDownStation().equals(section.getDownStation()))
            .findFirst()
            .ifPresent(it -> replaceSectionWithUpStation(section, it));
    }

    private void replaceSectionWithUpStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw IMPOSSIBLE_DISTANCE.makeException();
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(),
            existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw IMPOSSIBLE_DISTANCE.makeException();
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(),
            existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private List<Station> makeOrderedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section upEndSection = findUpEndSection();
        stations.add(upEndSection.getUpStation());

        Section nextSection = upEndSection;
        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findSectionByNextUpStation(nextSection.getDownStation());
        }

        return stations;
    }

    private Section findUpEndSection() {
        List<Station> downStations = this.sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        return this.sections.stream()
            .filter(it -> !downStations.contains(it.getUpStation()))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst()
            .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw ONLY_ONE_SECTION_EXIST.makeException();
        }

        Optional<Section> upSection = sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst();
        Optional<Section> downSection = sections.stream()
            .filter(it -> it.getDownStation().equals(station))
            .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            sections.add(new Section(newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    public Station startStation() {
        return orderedStations.get(0);
    }

    public Station endStation() {
        int size = orderedStations.size();
        return orderedStations.get(size - 1);
    }

    public int calculateTotalDistance() {
        return getSections().stream()
            .mapToInt(Section::getDistance)
            .sum();
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getOrderedStations() {
        return makeOrderedStations();
    }
}
