package wooteco.subway.line;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEqualValidator implements ConstraintValidator<NotEqual, Object> {
    private String upStationId;
    private String downStationId;

    @Override
    public void initialize(NotEqual constraintAnnotation) {
        this.upStationId = constraintAnnotation.upStationId();
        this.downStationId = constraintAnnotation.downStationId();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Object upStationValue = new BeanWrapperImpl(object).getPropertyValue(upStationId);
        Object downStationValue = new BeanWrapperImpl(object).getPropertyValue(downStationId);
        return !upStationValue.equals(downStationValue);
    }
}
