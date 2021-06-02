package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.User;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static wooteco.subway.path.domain.fare.DiscountPolicy.*;

public class FareTable {
    private Map<String, Integer> fareTable;
    private final int defaultFare;

    private FareTable(Map<String, Integer> fareTable, int defaultFare) {
        this.fareTable = fareTable;
        this.defaultFare = defaultFare;
    }

    public static FareTable of(Fare fare, User member) {
        Map<String, Integer> fareTable = new LinkedHashMap<>();
        Arrays.stream(values())
                .forEach(policy -> fareTable.put(policy.getKorean(), policy.apply(fare)));

        return new FareTable(fareTable, findByAge(fare, member));
    }

    private static int findByAge(Fare fare, User member) {
        if (member.isGuest()) {
            return ADULT.apply(fare);
        }
        DiscountPolicy memberDiscount = DiscountPolicy.findByAge(member.getAge());
        return memberDiscount.apply(fare);
    }

    public Map<String, Integer> getFareTable() {
        return fareTable;
    }

    public int getDefaultFare() {
        return defaultFare;
    }
}
