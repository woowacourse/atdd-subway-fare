package wooteco.subway.path.domain;

public class Price {
    int distance;

    public Price(int distance) {
        this.distance = distance;
    }

    public int getPrice() {
        if(distance <= 10){
            return 1250;
        }
        if(distance <= 50){
            return (int)(1250 + Math.ceil((distance - 10 - 1) / 5 + 1) * 100);
        }

        return (int)(2050 + Math.ceil((distance - 50 - 1) / 8 + 1) * 100);
    }
}
