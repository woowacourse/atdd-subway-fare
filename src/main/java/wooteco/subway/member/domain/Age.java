package wooteco.subway.member.domain;

public class Age {
    private final int age;

    public Age(final int age) {
        if (age < 0) {
            throw new IllegalArgumentException();
        }
        this.age = age;
    }

    public boolean isChildren() {
        return 6 <= age && age < 13;
    }

    public boolean isTeenager() {
        return 13 <= age && age < 19;
    }

    public int getAge() {
        return age;
    }
}
