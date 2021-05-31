package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationDao.isExistName(stationRequest.getName())) {
            throw new StationException("이미 존재하는 역 이름입니다.");
        }

        Station station = stationDao.insert(stationRequest.toStation());
        return toStationResponse(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new StationException("존재하지 않는 역입니다."));
    }

    public void deleteStationById(Long id) {
        stationDao.findById(id)
                .orElseThrow(() -> new StationException("존재하지 않는 역입니다."));

        stationDao.deleteById(id);
    }

    public void updateStationById(Long id, StationRequest stationRequest) {
        stationDao.findById(id)
                .orElseThrow(() -> new StationException("존재하지 않는 역입니다."));

        stationDao.updateById(id, stationRequest.getName());
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        stations.sort(Comparator.comparing(Station::getId));

        return toStationResponses(stations);
    }

    public List<StationResponse> toStationResponses(List<Station> stations) {
        return stations.stream()
                .map(this::toStationResponse)
                .collect(Collectors.toList());
    }

    public StationResponse toStationResponse(Station station) {
        return StationResponse.of(station, SimpleLineResponse.listOf(stationDao.findLinesPassing(station)));
    }
}
