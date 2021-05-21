package wooteco.subway.path.domain;

public class Price {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_PRICE = 1250;
    private static final int EXTRA_DISTANCE = 50;
    private static final int EXTRA_PRICE = 2050;
    private static final int UNIT_PRICE = 100;
    private static final int MAXIMUM_UNIT_DISTANCE = 8;
    private static final int MINIMUM_UNIT_DISTANCE = 5;

    private int price;

    public Price(int price) {
        this.price = price;
    }

    public void calculatePrice(int distance) {
        if(distance <= DEFAULT_DISTANCE){
            this.price =  DEFAULT_PRICE;
        }
        if(distance > DEFAULT_DISTANCE && distance <= EXTRA_DISTANCE){
            this.price = (int)(DEFAULT_PRICE + calculateExtraPrice(distance, DEFAULT_DISTANCE, MINIMUM_UNIT_DISTANCE));
        }
        if(distance > EXTRA_DISTANCE) {
            this.price = (int)(EXTRA_PRICE + calculateExtraPrice(distance, EXTRA_DISTANCE, MAXIMUM_UNIT_DISTANCE));
        }
        return;
    }

    private double calculateExtraPrice(int distance, int extraDistance, int unitDistance) {
        return Math.ceil((distance - extraDistance - 1) / unitDistance + 1) * UNIT_PRICE;
    }

    public int getPrice() {
        return price;
    }
}
