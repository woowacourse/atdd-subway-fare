package wooteco.subway.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NotEmpty
@Pattern(regexp = "^[0-9A-Za-z@$!%*?&]{8,14}$")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SubwayPassword {

}
