package wooteco.subway.member.domain;

public class Age {

    public static final int ADULT = 19;
    private static final int TEENAGER = 13;
    private static final int CHILD = 6;

    private final int age;

    public Age(int age) {
        this.age = age;
    }

    public boolean isAdult() {
        return age >= ADULT;
    }

    public boolean isTeenager() {
        return age >= TEENAGER && age < ADULT;
    }

    public boolean isChild() {
        return age >= CHILD && age < TEENAGER;
    }
}
