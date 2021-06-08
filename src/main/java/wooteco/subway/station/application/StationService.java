package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicatedStationException;
import wooteco.subway.station.exception.InvalidDeletionException;
import wooteco.subway.station.exception.NotFoundException;

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
        if (stationDao.exists(stationRequest.getName())) {
            throw new DuplicatedStationException(stationRequest.getName());
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 StationID 입니다. id: " + id));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        if (sectionDao.existsByStationId(id)) {
            throw new InvalidDeletionException("이미 노선에 등록되어있는 역은 지울 수 없습니다.");
        }
        stationDao.deleteById(id);
    }
}
