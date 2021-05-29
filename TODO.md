# 프론트와 협업한 부분
- 중복 처리 API - 이메일 중복처리만 가능한 API 추가
- 경로 조회 end point - 컨트롤러를 분리해서 /line 과 /map 을 따로 둔다.
### Error case
SignIn - 로그인에 실패한 경우

- code: 401
- message: 이메일 혹은 비밀번호를 다시 확인해주세요.

Lines - station에 대한 select 결과가 없는 경우

- code: 404
- message: 존재하지 않는 데이터입니다.

Sections - 구간 추가에 실패하는 경우
- code: 400
- message:

  - 상행과 하행역이 둘 다 없는 경우
  - 추가하려는 구간의 거리가 기존의 구간보다 크거나 같은 경우
  - 상행 종점을 찾지 못하는 경우
  - 구간이 하나여서 삭제하지 못하는 경우


### POST /members/email-check

Request
```
{
"email": "abcd@naver.com"
}
```
Response
```
409 Conflict
{
message: '이미 사용된 이메일입니다.'
}
```
```
200 OK
```

## Issue
미션 요구 사항 중 전체보기 기능에 대한 API 요청.
기존에 구현하셨던 노선 목록 조회 기능에 구간 정보를 추가해주시면 좋을 것 같습니다.

### Request
```
GET /map HTTP/1.1
Accept: application/json
Host: localhost:58561
```
### Response
```
200 ok

[ 
  {
    "id" : 1,
    "name" : "신분당선",
    "color" : "bg-red-600",
    "stations" : [ 
      {
        "id" : 1,
        "name" : "강남역"
      }, 
      ... // 역
    ],
    "sections" : [ 
      {
        "upStation" : {
          "id" : 1,
          "name" : "강남역"
        },
        "downStation" : {
          "id" : 2,
          "name" : "광교역"
        },
        "distance" : 10
      }, 
      ... // 구간
    ]
  } , 
  ... // 노선
]
```



# 기능 구현 목록

## 1단계
- [x] 설정 파일 나누기
    - [x] local 환경 (application-local.properties)
    - [x] production 환경 (application-prod.properties)
    - [x] test 환경 (application-test.properties)
    
## 2단계
- [x] map 기능 구현 

## 3단계
- [x] 기본운임(10㎞ 이내) : 기본운임 1,250원 
- [x] 이용 거리초과 시 추가운임 부과
    - [x] 10km초과∼50km까지(5km마다 100원)
    - [x] 50km초과 시 (8km마다 100원)

## 4단계
- [x] 로그인 유저의 경우 연령별 요금계산
    - [x] 0~6세인 경우
    - [x] 6~13세인 경우
    - [x] 13~19세인 경우
- [x] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - [x] ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    - [x] ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    - [x] ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원