package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;
import wooteco.subway.station.domain.Station;

public class StationOfSectionResponse {

    private final Long id;
    private final String name;

    public StationOfSectionResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    @ConstructorProperties({"id", "name"})
    public StationOfSectionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
