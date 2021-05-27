package wooteco.subway.web.dto.validator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;

@NotNull
@Positive
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface SubwayId {
}
