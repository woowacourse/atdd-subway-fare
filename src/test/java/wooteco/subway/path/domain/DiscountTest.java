package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.member.domain.Age;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {
    @DisplayName("나이 조건에 부합하는 Discount를 반환한다")
    @Test
    void getDiscount() {
        Age childrenAge = new Age(6);
        assertThat(Discount.getDiscount(childrenAge)).isEqualTo(Discount.CHILDREN);

        Age teenagerAge = new Age(13);
        assertThat(Discount.getDiscount(teenagerAge)).isEqualTo(Discount.TEENAGER);

        Age normalAge = new Age(19);
        assertThat(Discount.getDiscount(normalAge)).isEqualTo(Discount.NONE);
    }

    @DisplayName("전체 요금 금액과 할인 정책에 따라서 할인 금액을 계산해준다")
    @Test
    void calculateDiscountAmount() {
        int totalFare = 3500;

        assertThat(Discount.CHILDREN.calculateDiscountAmount(totalFare)).isEqualTo((int) ((totalFare -350) * 0.5));
        assertThat(Discount.TEENAGER.calculateDiscountAmount(totalFare)).isEqualTo((int) ((totalFare -350) * 0.2));
        assertThat(Discount.NONE.calculateDiscountAmount(totalFare)).isEqualTo(0);
    }
}