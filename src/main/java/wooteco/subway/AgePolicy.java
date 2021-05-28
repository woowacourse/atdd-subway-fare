package wooteco.subway;

public enum AgePolicy {
    CHILD_MINIMUM(6),
    CHILD_MAXIMUM(12),
    TEENAGE_MINIMUM(13),
    TEENAGE_MAXIMUM(18);

    private final int age;

    AgePolicy(int age) {
        this.age = age;
    }

    public int getAge(){
        return age;
    }

    public static boolean isChildren(int age) {
        return age >= CHILD_MINIMUM.getAge() && age <= CHILD_MAXIMUM.getAge();
    }

    public static boolean isTeenage(int age) {
        return age >= TEENAGE_MINIMUM.getAge() && age <= TEENAGE_MAXIMUM.getAge();
    }
}
