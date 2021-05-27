package wooteco.subway.member.domain;

import java.util.Objects;

public class Age {
    public static final int MINIMUM_CHILDREN_AGE = 6;
    public static final int MINIMUM_TEENAGER_AGE = 13;
    public static final int NON_INCLUSIVE_MAXIMUM_TEENAGER_AGE = 19;
    public static final int MINIMUM_POSSIBLE_AGE = 0;
    public static final int MAXIMUM_POSSIBLE_AGE = 200;
    private final int age;

    public Age(final int age) {
        if (age < MINIMUM_POSSIBLE_AGE || MAXIMUM_POSSIBLE_AGE < age) {
            throw new IllegalAgeException();
        }
        this.age = age;
    }

    public boolean isChildren() {
        return MINIMUM_CHILDREN_AGE <= age && age < MINIMUM_TEENAGER_AGE;
    }

    public boolean isTeenager() {
        return MINIMUM_TEENAGER_AGE <= age && age < NON_INCLUSIVE_MAXIMUM_TEENAGER_AGE;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Age age1 = (Age) o;
        return age == age1.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
