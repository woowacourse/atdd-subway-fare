package wooteco.subway.line.domain;

import wooteco.subway.path.domain.Distance;
import wooteco.subway.station.application.NoSuchStationException;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {
    public static final int DISTANCE_FOR_DOWN_END_STATION = -1;
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        List<Section> sortedSections = new ArrayList<>();

        if (sections.isEmpty()) {
            return sortedSections;
        }

        Section currentSection = findUpEndSection();
        while (currentSection != null) {
            sortedSections.add(currentSection);
            currentSection = findSectionByNextUpStation(currentSection.getDownStation());
        }

        return sortedSections;
    }

    public void addSection(Section section) {
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
        List<Station> stations = getStations();
        if (!stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation())) {
            throw new BothStationsNotExistException();
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new BothStationsAlreadyExistException();
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
        if (existSection.isShorterOrEqualTo(newSection)) {
            throw new RuntimeException();
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(), existSection.getSubtractedDistance(newSection)));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.isShorterOrEqualTo(newSection)) {
            throw new RuntimeException();
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), existSection.getSubtractedDistance(newSection)));
        this.sections.remove(existSection);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
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
                .map(it -> it.getDownStation())
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
            throw new UnableToRemoveSectionException();
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
            Distance newDistance = upSection.get().getDistance().add(downSection.get().getDistance());
            sections.add(new Section(newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    public Distance getDistanceToNextStation(final Station station) {
        try {
            Section section = sections.stream()
                    .filter(it -> it.hasUpStation(station))
                    .findAny()
                    .orElseThrow(NoSuchStationException::new);
            return section.getDistance();
        } catch (NoSuchStationException e) {
            return new Distance(DISTANCE_FOR_DOWN_END_STATION);
        }
    }

    public boolean contains(final Station station) {
        return sections.stream().anyMatch(it -> it.contains(station));
    }
}
