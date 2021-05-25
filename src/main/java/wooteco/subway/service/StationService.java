package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.common.exception.badrequest.DuplicateNameException;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
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

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    public StationResponse updateStation(Long id, StationRequest updateStationRequest) {
        String updateName = updateStationRequest.getName();
        if (stationDao.findByName(updateName).isPresent()) {
            throw new DuplicateNameException();
        }
        stationDao.update(id, updateName);
        return new StationResponse(id, updateName);
    }
}
