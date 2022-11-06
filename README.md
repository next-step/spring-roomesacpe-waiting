# spring-roomesacpe-waiting

## 1단계 - 인증 로직 리팩처링

### 프로그래밍 요구사항

- [x] auth 패키지를 nextstep 패키지로부터 분리한다
    - [x] auth 패키지에서 nextstep로 의존하는 부분을 제거한다.
    - [x] auth 패키지 내에서 스프링 빈으로 사용되던 객체를 Component Scan이 아닌 Java Configuration으로 빈 등록한다.

### 요구사항 설명

#### 패키지 분리

- auth 패키지를 nextstep 패키지로부터 분리한다.

#### Java Configuration 활용

- 기존에 Component Scan으로 등록하던 객체를 Java Configuration을 이용하여 등록한다.

## 2단계

### 상황 설명

- 예약 취소가 빈번해져서 다음 예약자를 구하기 번거로워졌다.
- 예약이 이뤄진 이후 대기를 신청할 수 있는 기능이 필요하다.

### 기능 요구사항

- [x] 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
    - [x] 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
- [x] 자신의 예약 대기를 취소할 수 있다.
    - [x] 자신의 예약 대기가 아닌경우 취소할 수 없다.
- [x] 나의 예약 목록을 조회할 수 있다.
    - [x] 예약과 예약 대기를 나눠서 조회한다.
    - [x] 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
    - [x] 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.
    - [x] 취소된 예약도 조회할 수 있다.

### 프로그래밍 요구사항

- [x] 트랜잭션을 설정하고 적절한 옵션을 이용한다.

### API 설계

#### 예약 대기

```http request
POST /reservation-waitings HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080

{
    "scheduleId": 1
}
```

```http request
HTTP/1.1 201 Created
Location: /reservation-waitings/1
```

#### 예약 목록 조회

```http request
GET /reservations/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```http request
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "schedule": {
            "id": 1,
            "theme": {
                "id": 1,
                "name": "테마이름",
                "desc": "테마설명",
                "price": 22000
            },
            "date": "2022-08-11",
            "time": "13:00:00"
        }
    }
]
```

#### 예약 대기 목록 조회

```http request
GET /reservation-waitings/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```http request
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "schedule": {
            "id": 3,
            "theme": {
                "id": 2,
                "name": "테마이름2",
                "desc": "테마설명2",
                "price": 20000
            },
            "date": "2022-08-20",
            "time": "13:00:00"
        },
        "waitNum": 12
    }
]
```

#### 예약 대기 취소

```http request
DELETE /reservation-waitings/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk5MDcwLCJleHAiOjE2NjMzMDI2NzAsInJvbGUiOiJBRE1JTiJ9.zgz7h7lrKLNw4wP9I0W8apQnMUn3WHnmqQ1N2jNqwlQ
```

```http request
HTTP/1.1 204
```

## 3단계 - 예약 승인

### 기능 요구사항

- [x] 예약 승인
    - [x] 예약 사용자가 예약금을 입금을 하면 예약 승인을 한다
    - [x] 이 때 예약금이 매출로 기록된다.
    - [x] 예약의 상태가 입금 대기에서 예약 승인 상태로 변경된다.
- [x] 사용자 예약 취소
    - [x] 사용자가 예약을 취소하는 경우 예약 미승인 상태와 승인 상태로 구분이 된다.
    - [x] 예약 미승인 상태면 예약의 상태가 예약 철회로 변경된다.
    - [x] 예약이 승인된 상태라면 관리자의 승인이 필요하다.
        - [ ] 관리자가 예약 취소를 승인하면 예약 취소가 진행된다.
        - [x] 매출에 환불이 기록된다.
        - [x] 예약의 상태가 예약 철회로 변경된다.
        - [x] 예약 대기자가 있는 경우 예약자로 변경할 수 있다.
- [x] 관리자 예약 취소
    - [x] 관리자도 예약을 취소할 수 있는데 이 역시 예약 미승인 상태와 승인 상태로 구분이 된다.
    - [x] 예약 미승인 상태면 예약의 상태가 예약 취소된다.
        - [x] 사용자 예약 취소와 같은 프로세스를 진행한다.

### 프로그래밍 요구사항

- [ ] 트랜잭션을 설정하고 적절한 옵션을 이용한다.

### API 설계

#### 예약 승인 (관리자)

```http request
PATCH /reservations/1/approve HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080
```

```http request
HTTP/1.1 200
```

#### 예약 취소 (사용자 + 관리자)

```http request
PATCH /reservations/1/cancel HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```http request
HTTP/1.1 200
```

#### 예약 취소 승인 (관리자)

```http request
GET /reservations/1/cancel-approve HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```http request
HTTP/1.1 200
```
