package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.badrequest.NotRemovalStationException;
import wooteco.common.exception.badrequest.StationDuplicateNameException;
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
    private StationDao stationDao;
    private SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        String name = stationRequest.getName();
        if (stationDao.findByName(name)
                .isPresent()) {
            throw new StationDuplicateNameException(name);
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
        if (sectionDao.existStation(id)) {
            throw new NotRemovalStationException(id);
        }
        stationDao.deleteById(id);
    }

    @Transactional
    public StationResponse updateStation(Long id, String name) {
        if (stationDao.findByName(name)
                .isPresent()) {
            throw new StationDuplicateNameException(name);
        }
        stationDao.update(new Station(id, name));
        return new StationResponse(id, name);
    }
}
