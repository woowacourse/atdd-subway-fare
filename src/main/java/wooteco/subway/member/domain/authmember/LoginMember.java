package wooteco.subway.member.domain.authmember;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareByAgeStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenBabyStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenChildStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenTeenagerStrategy;

public class LoginMember implements AuthMember {

    private static final List<DiscountFareByAgeStrategy> DISCOUNT_FARE_BY_AGE_STRATEGIES
        = Arrays.asList(new DiscountFareWhenBabyStrategy(), new DiscountFareWhenChildStrategy(),
        new DiscountFareWhenTeenagerStrategy());

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public int discountFareByAge(int fare) {
        return distinguishAgeMember().discountFareByAge(fare);
    }

    private DiscountFareByAgeStrategy distinguishAgeMember() {
        return DISCOUNT_FARE_BY_AGE_STRATEGIES.stream()
            .filter(discountFareByAgeStrategy -> discountFareByAgeStrategy.isInAgeRange(age))
            .findAny()
            .orElseThrow(NotExistException::new);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
