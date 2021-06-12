package wooteco.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.exception.SectionException;
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
            throw new SectionException("노선에 하나도 존재하지 않는 구간을 추가할 수 없습니다.");
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new SectionException("상행역과 하행역이 모두 노선에 존재합니다.");
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
            throw new SectionException("기존의 구간보다 더 큰 거리의 새로운 구간을 추가할 수 없습니다.");
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(), existSection.getDistance() - newSection
                .getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new SectionException("기존의 구간보다 더 큰 거리의 새로운 구간을 추가할 수 없습니다.");
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), existSection.getDistance() - newSection
                .getDistance()));
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
                            .orElseThrow(() -> new SectionException("노선에 구간이 존재하지 않습니다."));
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
                            .filter(it -> it.getUpStation().equals(station))
                            .findFirst()
                            .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new SectionException("종점뿐인 노선에서 역을 제거할 수 없습니다.");
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
}
