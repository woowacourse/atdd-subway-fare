package wooteco.subway.map.dto;

import java.util.List;

public class MapDetailResponse {

    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<MapSectionDto> sections;

    public MapDetailResponse() {

    }

    public MapDetailResponse(Long id, String name, String color, int distance,
        List<MapSectionDto> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }

    public List<MapSectionDto> getSections() {
        return sections;
    }
}
