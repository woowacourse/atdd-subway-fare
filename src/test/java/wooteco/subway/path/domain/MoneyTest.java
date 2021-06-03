package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @DisplayName("돈 객체 생성된다.")
    @Test
    void create() {
        //when
        Money money = new Money(100);

        //then
        assertThat(money).isInstanceOf(Money.class);
        assertThat(money).isEqualTo(new Money(100));
    }

    @DisplayName("돈 객체를 음수 값으로 생성하면 예외 발생한다.")
    @Test
    void createByNullValue() {
        //when then
        assertThatThrownBy(() -> new Money(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("거리당 요금을 계산해준다.")
    @Test
    void calculateFareByDistance() {
        //given
        int defaultDistance = 10;
        int overDefaultDistance = 11;

        //when
        Money defaultFare = Money.calculateFareByDistance(defaultDistance);
        Money addedFare = Money.calculateFareByDistance(overDefaultDistance);

        //then
        assertThat(defaultFare).isEqualTo(Money.DEFAULT_FARE);
        assertThat(addedFare).isEqualTo(Money.DEFAULT_FARE.plus(new Money(100)));
    }

    @ParameterizedTest(name = "할인 계산 적용된다.")
    @CsvSource(value = {"ADULT,1250", "TEENAGER,720", "CHILD,450", "BABY,0"})
    void applyDiscount(DiscountPolicy discountPolicy, int expectedMoney) {
        //when
        Money discountMoney = Money.DEFAULT_FARE.applyDiscount(discountPolicy);

        //then
        assertThat(discountMoney).isEqualTo(new Money(expectedMoney));
    }

    @ParameterizedTest(name = "정적인 할인 이후 요금이 음수 인지 확인한다.")
    @CsvSource(value = {"300,301,true", "100,99,false", "0, 1000, true"})
    void afterDiscountIsNegative(int value, int staticDiscountMoney, boolean expectedResult) {
        //when
        boolean result = new Money(value).afterDiscountIsNegative(new Money(staticDiscountMoney));

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @DisplayName("돈에 할인율을 곱해서 할인된 값을 구할 수 있다.")
    @Test
    void multiple() {
        //given
        Money money = new Money(100);

        //when
        Money multipleMoney = money.multipleDiscountRate(0.5);

        //then
        assertThat(multipleMoney).isEqualTo(new Money(50));
    }

    @DisplayName("돈에 할인율을 곱해서 할인된 값의 결과가 음수면 예외가 발생한다.")
    @Test
    void multipleResultIsNegativeValue() {
        //given
        Money money = new Money(100);

        //when then
        assertThatThrownBy(() -> money.multipleDiscountRate(-1.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Money 객체끼리 뺄셈을 할 수 있다.")
    @Test
    void minus() {
        //given
        Money money = new Money(100);

        //when
        Money resultMoney = money.minus(new Money(100));

        //then
        assertThat(resultMoney).isEqualTo(Money.ZERO);
    }

    @DisplayName("Money 객체끼리 뺄셈을 한 결과가 음수면 예외가 발생한다.")
    @Test
    void minusResultIsNegativeValue() {
        //given
        Money money = new Money(100);

        //when then
        assertThatThrownBy(() -> money.minus(new Money(101)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Money 객체끼리 덧셈을 할 수 있다.")
    @Test
    void plus() {
        //given
        Money money = new Money(100);

        //when
        Money resultMoney = money.plus(new Money(100));

        //then
        assertThat(resultMoney).isEqualTo(new Money(200));
    }

    @DisplayName("Money 객체끼리 덧셈을 한 결과가 음수면 예외가 발생한다.")
    @Test
    void plusResultIsNegativeValue() {
        //given
        Money money = new Money(100);

        //when then
        assertThatThrownBy(() -> money.plus(new Money(-101)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}