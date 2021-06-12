package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "노선 이름은 빈칸일 수 없습니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 빈칸일 수 없습니다.")
    private String color;

    @NotNull(message = "역 아이디를 입력해야합니다.", groups = RequestType.ON_CREATE.class)
    private Long upStationId;

    @NotNull(message = "역 아이디를 입력해야합니다.", groups = RequestType.ON_CREATE.class)
    private Long downStationId;

    @NotNull(message = "거리를 입력해야합니다.", groups = RequestType.ON_CREATE.class)
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

    @Override
    public String toString() {
        return "LineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", extraFare=" + extraFare +
                '}';
    }
}
