package wooteco.subway.station.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.DuplicateNameException;
import wooteco.subway.exception.badrequest.IllegalDeleteException;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            Station station = stationDao.insert(stationRequest.toStation());
            return StationResponse.of(station);
        } catch (DuplicateKeyException e) {
            throw new DuplicateNameException("이미 존재하는 지하철 역입니다");
        }
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id).orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse updateStation(Long id, StationRequest stationRequest) {
        try {
            Station station = stationDao.findById(id).orElseThrow(StationNotFoundException::new);
            Station updateStation = station.update(stationRequest.getName());
            stationDao.update(updateStation);
            return StationResponse.of(updateStation);
        } catch (DuplicateKeyException e) {
            throw new DuplicateNameException("이미 존재하는 지하철 역입니다");
        }
    }

    public void deleteStationById(Long id) {
        if (sectionDao.existStation(id)) {
            throw new IllegalDeleteException("이미 노선에 등록된 지하철 역입니다");
        }
        stationDao.deleteById(id);
    }
}
