package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.NotPermittedException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationWithTransferResponse;
import wooteco.subway.station.exception.StationNotFoundException;

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
        if (stationDao.isExistByName(stationRequest.getName())) {
            throw new DuplicatedException(stationRequest.getName());
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        if (stationDao.isNotExistById(id)) {
            throw new StationNotFoundException(id + "역");
        }
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationWithTransferResponse> findAllStationWithTransferResponse() {
        return stationDao.findAllStationWithTransfer();
    }

    public void deleteStationById(Long id) {
        if (stationDao.isNotExistById(id)) {
            throw new StationNotFoundException(String.valueOf(id));
        }
        if (sectionDao.isIncludedInLine(id)) {
            throw new NotPermittedException("노선에 포함된 역이므로 삭제할 수 없습니다.");
        }
        stationDao.deleteById(id);
    }
}
