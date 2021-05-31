package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.infrastructure.exception.delete.StationDeleteException;
import wooteco.subway.infrastructure.exception.duplicate.StationDuplicatedException;
import wooteco.subway.infrastructure.exception.not_found.StationNotFoundException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dto.SectionEntity;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateInsert(stationRequest);
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateInsert(final StationRequest stationRequest) {
        Optional<Station> foundStation = stationDao.findByName(stationRequest.getName());
        if (foundStation.isPresent()) {
            throw new StationDuplicatedException();
        }
    }

    public Station findStationById(Long id) {
        return stationDao
                .findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        validateDeleteStation(id);
        stationDao.deleteById(id);
    }

    private void validateDeleteStation(Long id) {
        Long validatedId = validateStationExistAndGetId(id);
        List<SectionEntity> sectionEntities = sectionDao.findSectionByStationId(validatedId);

        if (!sectionEntities.isEmpty()) {
            throw new StationDeleteException();
        }
    }


    public Long validateStationExistAndGetId(Long id) {
        return stationDao
                .findById(id)
                .orElseThrow(StationNotFoundException::new)
                .getId();
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        Long validatedId = validateStationExistAndGetId(id);
        validateInsert(stationRequest);
        stationDao.update(validatedId, stationRequest.toStation());
    }
}
