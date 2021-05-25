## 스프링 협업 미션
<hr>

요금 계산 요구사항

* [x] 10km까지는 기본 운임 1250원을 적용한다.
* [x] 기본 운임 거리 초과시 추가 요금을 부과한다.
    - [x] 10km 초과 ~ 50km 이하 까지는 5km 마다 100원 추가
    - [x] 50km 초과 시 8km 마다 100원 추가
    
FareTable - distance 를 줬을 때 요금 계산 해주는 아이
Money - 돈을 포장하는 객체
Distance - 거리를 포장하는 객체
  - new FareTable(distance); -> Fare 객체 생성 -> Money

최소경로 Response -> 비회원인 경우
- List<Station> stations
- int distance
- int defaultFare (성인 요금)
- Map<String, Integer> fare