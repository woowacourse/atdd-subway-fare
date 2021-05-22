# 스프링 - 협업 미션

## 🚀 1단계 - 배포하기

배포 URI : `https://inbi-subway.kro.kr`

## **🚀 2단계 - 전체 조회 기능**

Request

```json
GET /lines HTTP/1.1
Accept: application/json
Host: localhost:58561
```

Response

```json
HTTP/1.1 200 OK
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 19 May 2021 00:31:17 GMT
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 900

[
    {
        "id": 1,
        "name": "신분당선",
        "color": "red lighten-1",
        "extraFare": 100,
        "stations": [
            {
                "id": 1,
                "name": "강남역"
            },
            {
                "id": 2,
                "name": "판교역"
            },
            {
                "id": 3,
                "name": "정자역"
            }
        ]
    },
    {
        "id": 2,
        "name": "2호선",
        "color": "green lighten-1",
        "extraFare": 200,
        "stations": [
            {
                "id": 1,
                "name": "강남역"
            },
            {
                "id": 4,
                "name": "역삼역"
            },
            {
                "id": 5,
                "name": "잠실역"
            }
        ]
    }
]
```

## 🚀 3단계 - 요금 조회 기능

- 기본 요금(10km 이하) : 1,250원
- 이용 거리 초과 시 추가 요금 부과
  - 지하철 요금은 최단경로 총 거리 기준으로 부과된다.
  - 10km 초과 ∼ 50km 이하 (5km마다 100원씩 추가로 부과된다.)
  - 50km 초과 시 (8km마다 100원씩 추가로 부과된다.)

```text
9km = 1,250원
12km = 10km(1,250원) + 2km(100원) = 1,350원
16km = 10km(1,250원) + 6km(200원) = 1,450원
```


## 🚀 4단계 - 요금 정책 추가

### 노선별 추가 요금

- 경로 중 추가요금이 있는 노선을 이용 할 경우, 가장 높은 금액의 추가 요금만 적용한다.

```text
ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 + 900원 -> 2,150원
ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 + 900원 -> 2,250원
ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 + 900원 -> 2,150원
```

- LINE 테이블에 추가 요금 컬럼 추가
```sql
create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    extra_fare int not null default 0,  <---- 컬럼 추가
    primary key(id)
);
```


### 로그인 사용자의 경우 연령별 요금으로 계산

- 청소년(13세 이상~19세 미만) : 총 요금에서 350원을 뺀 후, 20%를 할인한 요금 적용

- 어린이(6세 이상~ 13세 미만) : 비용에서 350원을 뺀 후, 50%를 할인한 요금 적용

### 경로 조회 시 로그인 사용자 처리

- 기존 경로 조회 시 로그인 정보를 처리하지 않고 있었다.
- 경로 조회 기능은 비로그인 사용자도 사용할 수 있어야 한다.
  - 로그인 상태에서만 사용자의 나이로 연령별 요금을 적용한다.
  - 비로그인 상태에서는 연령별 요금을 적용하지 않는다.