package wooteco.subway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayApplication.class, args);
	}

}


/**
 stations의 리스트 -> 1 -> 4 -> 3
 extraprice <-
 쿼리 1 -> 4 Section -> LineId -> extraprice (upstation, downstation 기반으로 해당 라인의 추가 요금)
 max(extrapirce)
 **/