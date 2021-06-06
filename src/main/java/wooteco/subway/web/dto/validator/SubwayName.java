package wooteco.subway.web.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SubwayNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubwayName {
    String message() default "SubwayName 에러";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
