package wooteco.subway.web.dto.request;

import javax.validation.constraints.NotEmpty;
import wooteco.subway.web.dto.validator.SubwayName;

public class LineUpdateRequest {
    @SubwayName
    private String name;
    @NotEmpty
    private String color;
    public LineUpdateRequest() {
    }
    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
}