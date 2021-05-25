# 3단계 - 요금 조회 기능 구현 목록
## 경로 조회 시 요금 정보 포함하기
- 요금 계산 방법
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
- 10km초과∼50km까지(5km마다 100원)
- 50km초과 시 (8km마다 100원)

### 3단계 요금 계산 구현 목록
- [x] 경로가 10km 이하인 경우 요금은 1,250원이다.
- [x] 경로가 10km초과 ~ 50km 이하인 경우 100원 * 5km의 추가 운임이 발생한다.
- [x] 경로가 50km초과인 경우 100원 * 8km 의 추가 운임이 발생한다.



### 비회원
- Request
```
GET /api/paths?source=1&target=3 HTTP/1.1
```

- Response
```
HTTP/1.1 200 OK

{
  "stations" : [ {
    "id" : 1,
    "name" : "교대역"
  }, {
    "id" : 4,
    "name" : "남부터미널역"
  }, {
    "id" : 3,
    "name" : "양재역"
  } ],
  "distance" : 5,
  "defaultFare" : 성인요금
  "fare" : {
     "성인" : ...,
     "청소년" : ...,
     "어린이" : ...,
     "유아" : 0
  }
}
```


### 회원
- Request
```
GET /api/paths?source=1&target=3 HTTP/1.1
Authorization: Bearer ...
```

- Response
```
HTTP/1.1 200 OK

{
  "stations" : [ {
    "id" : 1,
    "name" : "교대역"
  }, {
    "id" : 4,
    "name" : "남부터미널역"
  }, {
    "id" : 3,
    "name" : "양재역"
  } ],
  "distance" : 5,
  "defaultFare" : 회원 정보에 따른 요금
  "fare" : { 
     "성인" : ...,
     "청소년" : ...,
     "어린이" : ...,
     "유아" : 0
  }
}
```