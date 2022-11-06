# spring-roomesacpe-waiting

<br/>

## 1단계
### 프로그래밍 요구사항
#### auth 패키지를 nextstep 패키지로부터 분리
- [x] auth 패키지 -> nextstep 패키지로 의존하는 부분을 제거한다.
- [x] auth 패키지에서 Spring Bean으로 사용되는 객체를 Component Scan이 아닌 `Java Configuration`으로 Bean 등록한다.

<br/>

## 2단계
### 기능 요구사항
- 예약 대기
  - [x] 이미 예약된 스케줄을 대상으로 예약 대기를 신청할 수 있다.
  - [x] 아직 예약이 되지 않은 스케줄을 대상으로 예약 대기를 신청하면, 예약이 된다.
- 예약 대기 취소
  - [x] 자신의 예약 대기를 취소할 수 있다.
  - [x] 자신의 예약 대기가 아니면, 취소할 수 없다.
- 나의 예약 목록 조회
  - 예약
    - [x] reservation
    - [x] 취소된 예약도 조회할 수 있다.
  - 예약 대기
    - [x] reservation-waiting
    - [x] `대기 순번`도 함께 조회할 수 있다.

### 프로그래밍 요구사항
- [x] 트랜잭션을 설정하고, 적절한 옵션을 이용한다.
