package wooteco.subway.line.domain;

import wooteco.subway.exception.badrequest.BadRequest;
import wooteco.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            throw new BadRequest("유효하지 않는 요청 값입니다");
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays.asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new BadRequest("유효하지 않는 요청 값입니다");
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
        checkSectionDistance(newSection, existSection);
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(), existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        checkSectionDistance(newSection, existSection);
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void checkSectionDistance(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new BadRequest("유효하지 않는 요청 값입니다");
        }
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

    public List<Section> getSortSections() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Section> sections = new ArrayList<>();
        Section upEndSection = findUpEndSection();

        Section nextSection = upEndSection;
        while (nextSection != null) {
            sections.add(nextSection);
            nextSection = findSectionByNextUpStation(nextSection.getDownStation());
        }

        return sections;
    }

    private Section findUpEndSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(it -> !downStations.contains(it.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new BadRequest("지하철 종점이 존재하지 않습니다."));
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst()
                .orElse(null);
    }

    public void removeStation(Station station) {
        checkExistStation(station);
        if (sections.size() <= 1) {
            throw new BadRequest("지하철 구간이 1개 이하일 때 더 이상 삭제할 수 없습니다.");
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

    private void checkExistStation(Station station) {
        getStations().stream()
                .filter(it -> it.equals(station))
                .findAny()
                .orElseThrow(() -> new BadRequest("유효하지 않는 요청 값입니다"));
    }

    public Stream<Section> stream() {
        return sections.stream();
    }
}
