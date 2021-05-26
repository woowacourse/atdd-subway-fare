package wooteco.subway.member.domain;

import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.member.exception.SubwayMemberException;

public class Member {

    private static final Pattern PATTERN =
        Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private static final int PASSWORD_MIN_LENGTH = 2;

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
    }

    public Member(Long id, String email, String password, Integer age) {
        validate(email, password, age);
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    private void validate(String email, String password, Integer age) {
        validateEmail(email);
        validatePassword(password);
        validateAge(age);
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || !(PATTERN.matcher(email).matches())) {
            throw new SubwayCustomException(SubwayMemberException.INVALID_EMAIL_EXCEPTION);
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || password.length() < PASSWORD_MIN_LENGTH) {
            throw new SubwayCustomException(SubwayMemberException.INVALID_PASSWORD_EXCEPTION);
        }
    }

    private void validateAge(Integer age) {
        if (Objects.isNull(age) || age <= 0 ) {
            throw new SubwayCustomException(SubwayMemberException.INVALID_AGE_EXCEPTION);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
}
