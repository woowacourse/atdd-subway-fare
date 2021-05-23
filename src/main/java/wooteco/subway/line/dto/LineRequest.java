package wooteco.subway.line.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "이름에 공백이 있을 수 없습니다.")
    @Length(min = 3 ,max = 20,  message = "노선 이름은 3이상 20이하 합니다.")
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
