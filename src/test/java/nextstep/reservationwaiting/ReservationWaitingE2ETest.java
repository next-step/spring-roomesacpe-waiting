package nextstep.reservationwaiting;

import nextstep.AbstractE2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationWaitingE2ETest extends AbstractE2ETest {

    @DisplayName("예약이 있는 스케줄에 예약 대기를 생성한다")
    @Test
    void createToBookedSchedule() {

    }

    @DisplayName("예약이 없는 스케줄에 예약 대기하면 예약이 된다")
    @Test
    void createToEmptySchedule() {

    }

    @DisplayName("자신의 예약 대기를 취소할 수 있다")
    @Test
    void cancelMine() {

    }

    @DisplayName("자신의 예약 대기가 아니면 취소할 수 없다")
    @Test
    void cancelNotMine() {

    }

    // TODO ggyool 순번도 비교해야함
    @DisplayName("자신의 예약 목록을 조회할 수 있다")
    @Test
    void readMine() {

    }

    @DisplayName("취소된 예약 대기 목록을 조회할 수 있다")
    @Test
    void readCanceledWaitings() {

    }

    @DisplayName("자신의 예약 대기가 아니면 목록을 조회할 수 없다")
    @Test
    void readNotMine() {

    }
}
