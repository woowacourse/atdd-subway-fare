package wooteco.subway.line.domain;

import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {
    private List<Section> sections = new ArrayList<>();

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

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        if (!stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation())) {
            throw new InvalidInputException("노선에 등록하려는 구간의 역들이 포함되어있지 않습니다.");
        }
    }

    private void checkAlreadyExisted(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new DuplicateException("이미 등록된 구간입니다");
        }
    }

    private void addSectionUpToUp(Section section) {
        this.sections.stream()
                .filter(it -> it.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> replaceSectionWithDownStation(section, it));
    }

    private void addSectionDownToDown(Section section) {
        this.sections.stream()
                .filter(it -> it.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> replaceSectionWithUpStation(section, it));
    }

    private void replaceSectionWithUpStation(Section newSection, Section existSection) {
        validateDistance(newSection, existSection);
        int distance = existSection.adjustDistance(newSection);
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(), distance));
        this.sections.remove(existSection);
    }

    private void validateDistance(Section newSection, Section existSection) {
        if (existSection.isShorter(newSection)) {
            throw new InvalidInputException("등록하려는 구간의 길이가 기존의 구간보다 깁니다. " +
                    "(기존 구간의 길이 " + existSection.getDistance() + ")");
        }
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        validateDistance(newSection, existSection);
        int distance = existSection.adjustDistance(newSection);
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), distance));
        this.sections.remove(existSection);
    }

    public List<Station> getStations() {
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
                .filter(it -> it.isSameUpStation(station))
                .findFirst()
                .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new InvalidInputException("구간이 하나일 경우 삭제할 수 없습니다.");
        }

        Optional<Section> upSection = sections.stream()
                .filter(it -> it.isSameUpStation(station))
                .findFirst();
        Optional<Section> downSection = sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int distance = upSection.get().increaseDistance(downSection.get());
            sections.add(new Section(newUpStation, newDownStation, distance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    public int totalDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }

    public List<Section> getSections() {
        return sections;
    }
}
