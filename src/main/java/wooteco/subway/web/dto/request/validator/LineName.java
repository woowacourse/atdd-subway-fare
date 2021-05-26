package wooteco.subway.web.dto.request.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NotEmpty
@Pattern(regexp = "^[가-힣0-9]+$")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface LineName {

}
