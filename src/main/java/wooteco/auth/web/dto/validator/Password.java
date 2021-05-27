package wooteco.auth.web.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Password {
    String message() default "잘못된 비밀번호 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minSize();

    int maxSize();
}
