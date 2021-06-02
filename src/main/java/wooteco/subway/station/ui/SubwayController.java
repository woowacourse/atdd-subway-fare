package wooteco.subway.station.ui;

import wooteco.subway.line.application.LineService;
import wooteco.subway.station.application.StationService;

public abstract class SubwayController {

    public StationService stationService;
    public LineService lineService;

    public SubwayController(StationService stationService,
        LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

}
