## 기능구현목록
### 1단계 - 인증 로직 리팩터링
- [ ] auth 패키지를 nextstep 패키지로부터 분리
  - [ ] auth 패키지에서 nextstep으로 의존하는 부분 제거
  - [ ] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration을 이용하도록 수정

### 2단계 - 예약 대기

### 3단계 - 예약 승인
