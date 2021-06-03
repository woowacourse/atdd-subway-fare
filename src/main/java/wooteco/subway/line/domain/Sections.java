package wooteco.subway.line.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import wooteco.subway.line.exception.InvalidSectionException;
import wooteco.subway.station.domain.Station;

public class Sections {
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
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
            throw new InvalidSectionException("유효하지 않는 요청 값입니다");
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new InvalidSectionException("유효하지 않는 요청 값입니다");
        }
    }

    private void addSectionUpToUp(Section section) {
        this.sections.stream()
                .filter(it -> it.isUpstationEqualsTo(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> replaceSectionWithDownStation(section, it));
    }

    private void addSectionDownToDown(Section section) {
        this.sections.stream()
                .filter(it -> it.isDownStationEqualsTo(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> replaceSectionWithUpStation(section, it));
    }

    private void replaceSectionWithUpStation(Section newSection, Section existSection) {
        if (existSection.isShorterThan(newSection)) {
            throw new InvalidSectionException("유효하지 않는 요청 값입니다");
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(), existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.isShorterThan(newSection)) {
            throw new InvalidSectionException("유효하지 않는 요청 값입니다");
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), existSection.getDistance() - newSection.getDistance()));
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

    public List<Section> getSortedSections() {
        return sort(sections);
    }

    private List<Section> sort(List<Section> sections) {
        Queue<Section> waiting = new LinkedList<>(sections);
        Deque<Section> sorted = new ArrayDeque<>();

        sorted.addLast(waiting.poll());
        sortWaiting(waiting, sorted);

        return new ArrayList<>(sorted);
    }

    private void sortWaiting(Queue<Section> waiting, Deque<Section> sorted) {
        while (!waiting.isEmpty()) {
            sortInAscendingOrder(waiting, sorted);
        }
    }

    private void sortInAscendingOrder(Queue<Section> waiting, Deque<Section> sorted) {
        Section current = waiting.poll();
        Section first = sorted.peekFirst();
        Section last = sorted.peekLast();

        if (current.isBefore(first)) {
            sorted.addFirst(current);
            return;
        }
        if (current.isAfter(last)) {
            sorted.addLast(current);
            return;
        }
        waiting.add(current);
    }

    private Section findUpEndSection() {
        List<Station> downStations = this.sections.stream()
                .map(it -> it.getDownStation())
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(it -> !downStations.contains(it.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new InvalidSectionException("유효하지 않는 요청 값입니다"));
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isUpstationEqualsTo(station))
                .findFirst()
                .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new InvalidSectionException("유효하지 않는 요청 값입니다");
        }

        Optional<Section> upSection = sections.stream()
                .filter(it -> it.isUpstationEqualsTo(station))
                .findFirst();
        Optional<Section> downSection = sections.stream()
                .filter(it -> it.isDownStationEqualsTo(station))
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
}
