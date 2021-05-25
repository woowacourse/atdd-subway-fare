package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "노선 이름은 빈칸일 수 없습니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 빈칸일 수 없습니다.")
    private String color;

    @Positive(message = "잘못된 역 아이디입니다.", groups = RequestType.ON_CREATE.class)
    private Long upStationId;

    @Positive(message = "잘못된 역 아이디입니다.", groups = RequestType.ON_CREATE.class)
    private Long downStationId;

    @Positive(message = "잘못된 거리 요청입니다.", groups = RequestType.ON_CREATE.class)
    private int distance;

    private Integer extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }
}
