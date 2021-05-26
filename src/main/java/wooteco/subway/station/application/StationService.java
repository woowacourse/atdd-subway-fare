package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.HttpException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

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
            throw new HttpException(HttpStatus.BAD_REQUEST, "이미 존재하는 역 이름 입니다.");
        }
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
        Station foundStation = stationDao.findById(id);
        if (foundStation == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "존재하지 않는 역입니다.");
        }
        if (sectionDao.countByStationId(id) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "노선에 포함된 역은 삭제할 수 없습니다.");
        }
        stationDao.deleteById(id);
    }
}
