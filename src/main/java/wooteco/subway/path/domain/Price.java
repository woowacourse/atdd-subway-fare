package wooteco.subway.path.domain;

public class Price {
    private int price;

    public Price(int distance) {
        this.price = calculatePrice(distance);
    }

    public int calculatePrice(int distance) {
        if(distance <= 10){
            return 1250;
        }
        if(distance <= 50){
            return (int)(1250 + Math.ceil((distance - 10 - 1) / 5 + 1) * 100);
        }
        return (int)(2050 + Math.ceil((distance - 50 - 1) / 8 + 1) * 100);
    }

    public int getPrice() {
        return price;
    }
}
