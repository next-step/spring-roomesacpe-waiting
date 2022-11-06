# spring-roomesacpe-waiting

## Step1 : 추상화와 관심사 분리

### Auth 패키지를 nextstep 패키지로부터 분리한다
- auth 패키지에서 nextstep로 의존하는 부분을 제거한다.

## Step

## Step2 : 기능 추가

### 명세

- 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
    - 예약이 없는 스케줄에 대해서, 예약 대기 조차 없다면 예약 대기 신청을 할 경우 예약이 된다.
    - 이미 해당 유저가 해당 스케줄에 예약이 있는 경우 예약 대기를 할 수 없다.
    - 이미 해당 유저가 해당 스케줄에 예약 대기가 있는 경우 예약 대기를 할 수 없다.
- 자신의 예약 대기를 취소할 수 있다.
  - 자신의 예약 대기가 아닌경우 취소할 수 없다.
  - 예약 대기가 취소된 경우, 다음 순번의 예약 대기자들의 순서가 앞당겨진다.
  - Domain name 
    - Wait-Number : 사용자가 확인하는 본인의 대기 순번
    - Sequence-Number : 사용자가 예약 대기에 걸어둔 순번
    - 즉, 예약 취소시 Wait-number는 변하지만, Sequence-number는 변하지 않는다.
- 나의 예약 목록을 조회할 수 있다.
    - 예약과 예약 대기를 나눠서 조회한다.
    - 예약은 reservation을 조회하고 예약 대기는 reservation-waiting을 조회한다.
    - 예약 대기의 경우 대기 순번도 함께 조회할 수 있다.
    - 취소된 예약도 조회할 수 있다.

### API 

#### 예약 대기 
```
POST /reservation-waitings HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080

{
    "scheduleId": 1
}
```

```
HTTP/1.1 201 Created
Location: /reservation-waitings/1
```

#### 예약 조회 
```
GET /reservations/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
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
```
GET /reservation-waitings/mine HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
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

```
DELETE /reservation-waitings/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk5MDcwLCJleHAiOjE2NjMzMDI2NzAsInJvbGUiOiJBRE1JTiJ9.zgz7h7lrKLNw4wP9I0W8apQnMUn3WHnmqQ1N2jNqwlQ
```

```
HTTP/1.1 204 
```
