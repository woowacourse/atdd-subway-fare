package wooteco.subway.line.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.station.domain.Station;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections empty() {
        return new Sections(new ArrayList<>());
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
        if (!stations.contains(section.getUpStation()) && !stations
            .contains(section.getDownStation())) {
            throw new DuplicateException("이미 존재하는 구간입니다.");
        }
    }

    private void checkExistedAny(Section section) {
        List<Station> stations = getStations();
        List<Station> stationsOfNewSection = Arrays
            .asList(section.getUpStation(), section.getDownStation());
        if (stations.containsAll(stationsOfNewSection)) {
            throw new DuplicateException("이미 존재하는 구간입니다.");
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
            throw new SubwayException("구간의 길이가 잘못됐습니");
        }
        this.sections.add(new Section(existSection.getUpStation(), newSection.getUpStation(),
            existSection.getDistance() - newSection.getDistance()));
        this.sections.remove(existSection);
    }

    private void replaceSectionWithDownStation(Section newSection, Section existSection) {
        if (existSection.getDistance() <= newSection.getDistance()) {
            throw new SubwayException("구간의 길이가 잘못됐습니다.");
        }
        this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(),
            existSection.getDistance() - newSection.getDistance()));
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
            .collect(toList());

        return this.sections.stream()
            .filter(it -> !downStations.contains(it.getUpStation()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("구간을 찾을 수 없습니다."));
    }

    private Section findSectionByNextUpStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst()
            .orElse(null);
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new SubwayException("구간은 하나 이상 존재해야 합니다.");
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

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    public void updateDistance(Long upStationId, Long downStationId, Integer distance) {
        validateDistance(distance);

        Section targetSection = findSectionByUpStationIdAndDownStationId(upStationId,
            downStationId);
        sections.remove(targetSection);
        sections.add(
            new Section(
                targetSection.getId(),
                targetSection.getUpStation(),
                targetSection.getDownStation(),
                distance
            )
        );
    }

    private void validateDistance(Integer distance) {
        if (distance <= 0) {
            throw new SubwayException("구간 사이 거리는 0보다 작거나 같을 수 없습니다.");
        }
    }

    private Section findSectionByUpStationIdAndDownStationId(Long upStationId, Long downStationId) {
        return sections.stream()
            .filter(section -> section.hasSameDownStationId(downStationId))
            .filter(section -> section.hasSameUpStationId(upStationId))
            .findAny()
            .orElseThrow(() -> new NotFoundException("일치하는 구간을 찾을 수 없습니다."));
    }

    public List<Section> getSections() {
        return getStations().subList(0, sections.size()).stream()
            .map(this::findSectionByUpStation)
            .collect(toList());
    }

    private Section findSectionByUpStation(Station station) {
        return sections.stream()
            .filter(section -> section.getUpStation().equals(station))
            .findAny()
            .orElseThrow(() -> new NotFoundException("섹션 목록을 불러오는데 실패했습니다"));
    }

}
