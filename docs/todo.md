# 🚇 지하철 노선도 미션 - 기능 요구 사항

## 📌 STEP 1

### 1. 회원 기능

- 사용자는 회원 가입을 할 수 있다.
    - 필요 정보: 이메일, 나이, 비밀번호, 비밀번호 확인 정보
        - [ ] 나이는 1 ~ 200 이하의 숫자여야 한다.
        - [ ] 이메일은 이메일 형식이여야 한다.
        - [ ] 공백은 입력 할 수 없다.
        - [ ] 비밀번호는 최소 4글자에서 최대 20 글자여야 한다.
    - [ ] 가입시 이미 가입한 이메일인지 중복 확인

### 2. 지하철 역 관리 기능

- 사용자는 지하철 역을 추가할 수 있다.
    - [ ] 역 이름: 2자 이상 20자 이하의 한글 (숫자 포함. 공백 허용 X)
- 사용자는 지하철 역을 삭제할 수 있다.
    - [ ] 노선에 등록되어 있는 역인 경우 삭제할 수 없어야 한다.

### 3. 지하철 노선 관리 기능

- 사용자는 지하철 노선을 추가할 수 있다.
    - [ ] 노선 이름: 2자 이상 10자 이하의 한글 (숫자 포함. 공백 허용 X)
    - [ ] 상행역, 하행역: 기존에 등록되어 있는 지하철 역 목록 중에서 선택
        - [ ] 상행역과 하행역을 동일할 수 없다.
    - [ ] 색상: 미리 지정되어 있는 10가지 색상 중 한 색상 선택.
        - [ ] 다른 노선에서 사용하는 색은 선택 불가능