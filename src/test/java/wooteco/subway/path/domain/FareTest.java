package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @DisplayName("요금 객체 생성한다.")
    @Test
    void of() {
        //given
        Money money1 = new Money(0);
        Money money2 = new Money(100);

        //when
        Fare fare1 = Fare.of(money1);
        Fare fare2 = Fare.of(money2);

        //then
        assertThat(fare1).isEqualTo(Fare.of(new Money(0)));
        assertThat(fare2).isEqualTo(Fare.of(new Money(100)));
    }

    @DisplayName("음수의 값으로 요금 객체 생성하면 예외 발생한다.")
    @Test
    void createByNegativeValue() {
        //given
        assertThatThrownBy(() -> new Money(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("거리와 추가요금으로 요금 계산한다.")
    @Test
    void calculateFare() {
        //given
        Fare extraFare = Fare.of(new Money(300));

        //when
        Fare fare = Fare.calculateFare(10, extraFare);

        //then
        assertThat(fare).isEqualTo(Fare.of(new Money(1550)));
    }

    @DisplayName("요금을 int타입으로 가져올 수 있다.")
    @Test
    void money() {
        //given
        Fare fare = Fare.of(new Money(300));

        //when
        int money = fare.money();

        //then
        assertThat(money).isEqualTo(300);
    }

    @ParameterizedTest(name = "할인 정책에 따라 요금이 할인된다.")
    @CsvSource(value = {"ADULT,1250", "TEENAGER,720", "CHILD,450", "BABY,0"})
    void discount(DiscountPolicy discountPolicy, int money) {
        //given
        Fare fare = Fare.of(new Money(1250));

        //when
        Fare afterDistance = fare.discount(discountPolicy);

        //then
        assertThat(afterDistance.money()).isEqualTo(money);
    }

    @ParameterizedTest(name = "요금이 정적인 할인금액 보다 작을 때 할인을 적용하면 요금은 0원이다.")
    @CsvSource(value = {"ADULT,0,0,0", "TEENAGER,1,2,100", "CHILD,1,2,100", "BABY,0,0,0"})
    void discountTeenagerByNegative(DiscountPolicy discountPolicy, int value1, int value2, int value3) {
        //given
        Money staticDiscount = discountPolicy.getStaticDiscount();
        Fare fare1 = Fare.of(staticDiscount.minus(new Money(value1)));
        Fare fare2 = Fare.of(staticDiscount.minus(new Money(value2)));
        Fare fare3 = Fare.of(staticDiscount.minus(new Money(value3)));

        //when
        Fare afterDistance1 = fare1.discount(discountPolicy);
        Fare afterDistance2 = fare2.discount(discountPolicy);
        Fare afterDistance3 = fare3.discount(discountPolicy);

        //then
        assertThat(afterDistance1.money()).isEqualTo(0);
        assertThat(afterDistance2.money()).isEqualTo(0);
        assertThat(afterDistance3.money()).isEqualTo(0);
    }

    @ParameterizedTest(name = "나이에 맞는 요금을 계산해준다.")
    @CsvSource(value = {"19,1250", "18,720", "12,450", "5,0"})
    void discountByAge(int age, int money) {
        //given
        Fare fare = Fare.of(new Money(1250));

        //when
        Fare afterDistance = fare.discountByAge(age);

        //then
        assertThat(afterDistance.money()).isEqualTo(money);
    }
}