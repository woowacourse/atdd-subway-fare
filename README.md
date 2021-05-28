<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacuorse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-map">
</p>

<br>

# 지하철 노선도 미션
스프링 과정 실습을 위한 지하철 노선도 애플리케이션

<br>

## 📑 API Specification
6인 페어 [API 문서](https://da-nyee-subway-fare.kro.kr/swagger-ui.html)

<br>

## 👥 Team

### Backend
다니 마크 찰리 욘

### Frontend
체프 디토

<br>

## Requirements

> 3단계

- [x] 경로 조회 결과에 요금 정보를 포함하기

```
💸 요금 계산 방법

기본운임(10㎞ 이내) : 기본운임 1,250원
추가운임(10km 초과 ~ 50km 이내) : 5km마다 100원
추가운임(50km 초과) : 8km마다 100원
```

> 4단계

- [ ] 추가된 요금 정책을 반영하기

```
💰 노선별 추가 요금

추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가
ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원

경로 중 추가 요금이 있는 노선을 환승하여 이용할 경우 가장 높은 금액의 추가 요금만 적용
ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
```

```
💵 로그인 사용자의 경우 연령별 요금으로 계산

청소년(13세 이상 ~ 19세 미만): 운임에서 350원을 공제한 금액의 20%할인
어린이(6세 이상 ~ 13세 미만): 운임에서 350원을 공제한 금액의 50%할인
```

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
