package wooteco.subway.line.ui.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @NotBlank(message = "이름은 빈 값일 수 없습니다.")
    @Length(min = 2, max = 10)
    private final String name;
    @NotBlank(message = "색깔은 빈 값일 수 없습니다.")
    private final String color;
    @NotNull(message = "상행역은 빈 값일 수 없습니다.")
    private final Long upStationId;
    @NotNull(message = "하행역은 빈 값일 수 없습니다.")
    private final Long downStationId;
    private final int distance;
    private final int extraFare;

    @ConstructorProperties({"name", "color", "upStationId", "downStationId", "distance",
        "extraFare"})
    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance, int extraFare) {
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
