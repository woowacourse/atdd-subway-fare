package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.badrequest.NotRemovalStationException;
import wooteco.common.exception.badrequest.StationNameDuplicateException;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
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
    private final LineDao lineDao;

    public StationService(StationDao stationDao, SectionDao sectionDao,
        LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        if(stationDao.findByName(stationRequest.getName()).isPresent()) {
            throw new StationNameDuplicateException();
        }
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        final Lines lines = new Lines(lineDao.findAll());

        return stations.stream()
                .map(station -> StationResponse.of(station, lines.getLinesContainStation(station)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        if (sectionDao.existStation(id)) {
            throw new NotRemovalStationException();
        }
        stationDao.deleteById(id);
    }

    @Transactional
    public StationResponse updateStation(Long id, String name) {
        if (stationDao.findByName(name).isPresent()) {
            throw new StationNameDuplicateException();
        }
        stationDao.updateName(id, name);
        return new StationResponse(id, name);
    }
}
