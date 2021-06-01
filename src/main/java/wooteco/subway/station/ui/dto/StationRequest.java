package wooteco.subway.station.ui.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.station.domain.Station;

public class StationRequest {

    @NotBlank(message = "이름은 빈 값이 올 수 없습니다.")
    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|0-9|]+$", message = "역 이름은 한글과 숫자만 입력할 수 있습니다.")
    private final String name;

    @ConstructorProperties("name")
    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }

}
