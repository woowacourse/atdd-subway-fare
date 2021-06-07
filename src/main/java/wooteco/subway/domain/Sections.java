package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sections {

    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Section> sortedSections() {
        LinkedList<Section> sortedSection = new LinkedList<>();
        final Section pivotSection = sections.get(0);
        sortedSection.add(pivotSection);
        sortPreviousSections(sortedSection, pivotSection);
        sortFollowingSections(sortedSection, pivotSection);
        return sortedSection;
    }

    private void sortPreviousSections(LinkedList<Section> sortedSections, Section section) {
        final Station upStation = section.getUpStation();
        findSectionByDownStation(upStation)
            .ifPresent(sec -> {
                sortedSections.addFirst(sec);
                sortPreviousSections(sortedSections, sec);
            });
    }

    private Optional<Section> findSectionByDownStation(Station targetStation) {
        return sections.stream()
            .filter(section -> section.isDownStation(targetStation))
            .findAny();
    }

    private void sortFollowingSections(LinkedList<Section> sortedSections, Section section) {
        final Station downStation = section.getDownStation();
        findSectionByUpStation(downStation)
            .ifPresent(sec -> {
                sortedSections.addLast(sec);
                sortFollowingSections(sortedSections, sec);
            });
    }

    private Optional<Section> findSectionByUpStation(Station targetStation) {
        return sections.stream()
            .filter(section -> section.isUpStation(targetStation))
            .findAny();
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
        if (notExistStation(section, stations)) {
            throw new RuntimeException();
        }
    }

    private boolean notExistStation(Section section, List<Station> stations) {
        return !stations.contains(section.getUpStation()) &&
            !stations.contains(section.getDownStation());
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays
            .asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new RuntimeException();
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
            throw new RuntimeException();
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(),
            existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new RuntimeException();
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(),
            existSection.getDistance() - newSection.getDistance()));
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
            throw new RuntimeException();
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

    public boolean containsStation(Station station) {
        return sections
            .stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .anyMatch(st -> st.equals(station));
    }
}
