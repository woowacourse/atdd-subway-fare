package wooteco.subway.line.ui.dto.map;

import wooteco.subway.station.domain.Station;

import java.beans.ConstructorProperties;

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
