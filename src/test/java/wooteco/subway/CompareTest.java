package wooteco.subway;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class CompareTest {

    @Test
    void name() {
        Integer result = Stream.of(1, 2, 5, 4, 3)
                .max(Integer::compare)
                .orElse(0);

        assert result == 5;
    }
}
