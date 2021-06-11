package wooteco.subway.section.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.NoSuchSectionException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.section.domain.Section;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.section.dto.SectionResponse;
import wooteco.subway.section.dto.SectionServiceDto;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public SectionService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public SectionServiceDto saveByLineCreate(Line line, @Valid SectionServiceDto dto) {
        Section section = newSection(line, dto);
        return saveSectionAtEnd(section);
    }

    public SectionServiceDto save(Line line, @Valid SectionServiceDto dto) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));
        Section section = newSection(line, dto);
        sections.validateInsertable(section);

        if (sections.isBothEndSection(section)) {
            return saveSectionAtEnd(section);
        }
        return saveSectionAtMiddle(section, sections);
    }

    private Section newSection(Line line, @Valid SectionServiceDto dto) {
        Station upStation = stationService.findById(dto.getUpStationId());
        Station downStation = stationService.findById(dto.getDownStationId());
        Distance distance = new Distance(dto.getDistance());
        return new Section(line, upStation, downStation, distance);
    }

    private SectionServiceDto saveSectionAtEnd(Section section) {
        return SectionServiceDto.from(sectionDao.insert(section));
    }

    private SectionServiceDto saveSectionAtMiddle(Section section, Sections sections) {
        Section legacySection = sections.findByMatchStation(section);
        sectionDao.delete(legacySection);
        sectionDao.insert(legacySection.updateForSave(section));
        return SectionServiceDto.from(sectionDao.insert(section));
    }

    public void delete(Line line, @NotNull Long stationId) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));
        Station station = stationService.findById(stationId);
        sections.validateDeletableCount();
        sections.validateExistStation(station);

        if (sections.isBothEndStation(station)) {
            deleteStationAtEnd(line, station);
            return;
        }
        deleteStationAtMiddle(line, station);
    }

    private void deleteStationAtEnd(Line line, Station station) {
        if (sectionDao.findByLineIdAndUpStationId(line.getId(), station.getId()).isPresent()) {
            sectionDao.deleteByLineIdAndUpStationId(line.getId(), station.getId());
        }
        sectionDao.deleteByLineIdAndDownStationId(line.getId(), station.getId());
    }

    private void deleteStationAtMiddle(Line line, Station station) {
        Section upSection = sectionDao.findByLineIdAndDownStationId(line.getId(), station.getId())
            .orElseThrow(NoSuchSectionException::new);
        Section downSection = sectionDao.findByLineIdAndUpStationId(line.getId(), station.getId())
            .orElseThrow(NoSuchSectionException::new);

        Section updatedSection = upSection.updateForDelete(downSection);
        sectionDao.delete(upSection);
        sectionDao.delete(downSection);
        sectionDao.insert(updatedSection);
    }

    public List<StationResponse> findStationResponsesByLind(Line line) {
        Sections sections = findSectionsByLine(line);
        return StationResponse.listOf(new ArrayList<>(sections.sortedStations()));
    }

    public Sections findSectionsByLine(Line line) {
        return new Sections(sectionDao.findAllByLineId(line.getId()));
    }

    public List<SectionResponse> findSectionResponses(Line line) {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        Sections sections = findSectionsByLine(line);

        for (Station station : sections.sortedStations()) {
            int distance = sections.distanceValueWithNextStationOf(station);
            List<TransferLineResponse> transferLineResponses = findTransferLineResponses(station, line);
            sectionResponses.add(new SectionResponse(station.getId(), station.getName(), distance, transferLineResponses));
        }

        return sectionResponses;
    }

    private List<TransferLineResponse> findTransferLineResponses(Station station, Line line) {
        return sectionDao.findIncludeStationLine(station.getId())
            .stream()
            .filter(line::isNotEqual)
            .map(TransferLineResponse::from)
            .collect(Collectors.toList());
    }
}

