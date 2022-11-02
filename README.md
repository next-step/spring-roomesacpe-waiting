## 기능구현목록
### 1단계 - 인증 로직 리팩터링
- [x] auth 패키지를 roomescape 패키지로부터 분리
  - [x] auth 패키지에서 nextstep으로 의존하는 부분 제거
  - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration을 이용하도록 수정

### 2단계 - 예약 대기
- [x] 이미 예약이 된 스케줄에 예약 대기를 신청할 수 있다
  - [x] 예약이 없는 스케줄에 예약 대기를 신청할 경우, 예약이 된다
- [x] 자신의 예약 대기를 취소할 수 있다
  - [x] 자신의 예약 대기가 아닌 경우, 취소할 수 없다
- [ ] 나의 예약 목록을 조회할 수 있다
  - [ ] 예약과 예약 대기를 나눠서 조회한다
  - [ ] 예약은 revservation을 조회하고 예약 대기는 reservation-waiting을 조회한다
  - [ ] 예약 대기의 경우, 순번도 함께 조회한다
  - [ ] 취소된 예약도 조회할 수 있다

### 3단계 - 예약 승인
