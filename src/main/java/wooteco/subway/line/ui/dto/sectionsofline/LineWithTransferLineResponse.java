package wooteco.subway.line.ui.dto.sectionsofline;

import java.util.ArrayList;
import java.util.List;

public class LineWithTransferLineResponse {

    private final Long id;
    private final String color;
    private final String name;

    private final List<StationOfLineResponse> stationResponses;

    public LineWithTransferLineResponse(Long id, String color, String name,
        List<StationOfLineResponse> stationResponses) {
        this.id = id;
        this.color = color;
        this.name = name;

        this.stationResponses = new ArrayList<>(stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<StationOfLineResponse> getStationResponses() {
        return stationResponses;
    }

}
