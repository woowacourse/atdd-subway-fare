package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.badrequest.DuplicateStationNameException;
import wooteco.common.exception.badrequest.NotRemovalStationException;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
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
        Lines lines = new Lines(lineDao.findAll());

        return StationResponse.listOf(stations, lines);
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
        stationDao.findByName(updateName).ifPresent(
                station -> {
                    throw new DuplicateStationNameException();
                }
        );
        stationDao.update(id, updateName);
        return new StationResponse(id, updateName);
    }
}
