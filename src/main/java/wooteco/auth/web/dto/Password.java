package wooteco.auth.web.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password {

    String message() default "잘못된 패스워드 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minSize();

    int maxSize();
}
