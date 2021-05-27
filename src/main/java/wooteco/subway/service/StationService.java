package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.badrequest.DuplicateStationNameException;
import wooteco.common.exception.badrequest.NotRemovalStationException;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationDao.findByName(stationRequest.getName()).isPresent()) {
            throw new DuplicateStationNameException();
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        if (sectionDao.existsStation(id)) {
            throw new NotRemovalStationException();
        }
        stationDao.deleteById(id);
    }

    @Transactional
    public StationResponse updateStation(Long id, StationRequest updateStationRequest) {
        String updateName = updateStationRequest.getName();
        if (stationDao.findByName(updateName).isPresent()) {
            throw new DuplicateStationNameException();
        }
        stationDao.update(id, updateName);
        return new StationResponse(id, updateName);
    }
}
