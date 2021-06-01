package wooteco.subway.member.domain;

public class LoginMember implements AuthMember {

    private static final int TEENAGER_AGE_BOUNDARY = 13;
    private static final int CHILD_AGE_BOUNDARY = 6;
    private static final int ADULT_AGE_BOUNDARY = 19;
    private static final double DISCOUNT_RATE_FOR_CHILD = 0.5;
    private static final int DISCOUNT_PRICE_FOR_CHILD = 350;
    private static final int DISCOUNT_FOR_TEENAGER = 350;
    private static final double DISCOUNT_RATE_FOR_TEENAGER = 0.8;
    private static final int NO_FARE = 0;
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
        if (isBaby()) {
            return NO_FARE;
        }
        if (isChild()) {
            return (int) ((fare - DISCOUNT_PRICE_FOR_CHILD) * DISCOUNT_RATE_FOR_CHILD);
        }
        if (isTeenager()) {
            return (int) ((fare - DISCOUNT_FOR_TEENAGER) * DISCOUNT_RATE_FOR_TEENAGER);
        }
        return fare;
    }

    private boolean isBaby() {
        return age < CHILD_AGE_BOUNDARY;
    }

    private boolean isTeenager() {
        return TEENAGER_AGE_BOUNDARY <= age & age < ADULT_AGE_BOUNDARY;
    }

    private boolean isChild() {
        return CHILD_AGE_BOUNDARY <= age & age < TEENAGER_AGE_BOUNDARY;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
