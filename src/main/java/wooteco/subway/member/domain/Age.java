package wooteco.subway.member.domain;

public class Age {
    private static final int ANONYMOUS_USER_AGE = -1;

    private final int age;

    private Age(int age) {
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public static Age ageOfAnonymousUser() {
        return new Age(ANONYMOUS_USER_AGE);
    }

    public int getAge() {
        return age;
    }

    public boolean isAnonymousUserAge() {
        return this.age == ANONYMOUS_USER_AGE;
    }
}
